package com.argussec.sbt.openapi

import java.io.File

import org.apache.commons.io.FileUtils
import org.openapitools.codegen.DefaultGenerator
import org.openapitools.codegen.config.CodegenConfigurator
import sbt._
import sbt.util.Logger

import scala.collection.JavaConverters._

object OpenApiTasks {
  def copyFileList(filesToCopy: Map[String, String], baseSrcDir: File, baseDestDir: File, log: Logger): Seq[File] = {
    FileUtils.deleteDirectory(baseDestDir)

    if (!baseDestDir.mkdirs()) {
      log.error(s"Failed to recreate destination directory: $baseDestDir")
      throw new Exception(s"Cannot copy into $baseDestDir - directory could not be created")
    }

    filesToCopy.flatMap { case (src, dest) =>
      val fullSrc = baseSrcDir / src
      val fullDest = baseDestDir / dest

      if (!fullSrc.exists) {
        log.warn(s"Path does not exist: $fullSrc")
        Seq.empty
      } else {
        val destDir = fullDest.getParentFile

        if (!destDir.exists) {
          if (!destDir.mkdirs()) {
            log.error(s"Failed to call mkdirs on path: $destDir")
            throw new IllegalStateException(s"Cannot copy $src to $dest - failed to make destination directories")
          }
        }

        if (fullSrc.isDirectory) {
          log.info(s"Copying directory: $fullSrc to $fullDest")
          FileUtils.copyDirectory(fullSrc, fullDest)
          FileUtils.listFiles(fullDest, null, true).asScala
        } else {
          log.info(s"Copying file: $fullSrc to $fullDest")
          FileUtils.copyFile(fullSrc, fullDest)
          Seq(fullDest)
        }
      }
    }.toSeq
  }

  def generate(configurator: CodegenConfigurator): File = {
    val outputDir = new File(configurator.getOutputDir)
    FileUtils.deleteDirectory(outputDir)

    // Temporarily switch to the generator's class loader.
    // This is required because the generator does not use ServiceLoader properly.
    val oldClassLoader = Thread.currentThread.getContextClassLoader
    Thread.currentThread.setContextClassLoader(classOf[DefaultGenerator].getClassLoader)

    try {
      val input = configurator.toClientOptInput
      val config = input.getConfig

      for {
        (key, value) <- config.additionalProperties.asScala
        if value.isInstanceOf[String]
        strVal = value.asInstanceOf[String]
        if strVal.equalsIgnoreCase("true") || strVal.equalsIgnoreCase("false")
      } {
        config.additionalProperties.put(key, strVal.toBoolean: java.lang.Boolean)
      }

      new DefaultGenerator().opts(input).generate()
      outputDir
    } finally {
      Thread.currentThread.setContextClassLoader(oldClassLoader)
    }
  }
}
