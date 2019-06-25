package com.argussec.sbt.openapi

import com.argussec.sbt.openapi.OpenApiKeys._
import org.openapitools.codegen.CodegenConstants
import org.openapitools.codegen.config.{CodegenConfigurator, GeneratorProperties}
import sbt.Keys._
import sbt._

object OpenApiSettings {
  lazy val baseOpenApiSettings = Seq(
    sourceGenerators in Compile += OpenApiKeys.openapiCopySources.taskValue,
    resourceGenerators in Compile += OpenApiKeys.openapiCopyResources.taskValue,
    openapiConfigurationFile := None,
    openapiApiPackage := Keys.organization.value + ".openapi.generated.api",
    openapiModelPackage := Keys.organization.value + ".openapi.generated.model",
    openapiInvokerPackage := Keys.organization.value + ".openapi.generated.invoker",
    openapiSkipValidateSpec := false,
    openapiGenerateAliasAsModel := false,
    openapiAdditionalProperties := Map.empty,
    openapiGenerateApis := true,
    openapiGenerateModels := true,
    openapiModelsToGenerate := List.empty,
    openapiGenerateSupportingFiles := true,
    openapiSupportingFilesToGenerate := List.empty,
    openapiGenerateModelTests := true,
    openapiGenerateModelDocumentation := true,
    openapiGenerateApiTests := true,
    openapiGenerateApiDocumentation := true,
    openapiVerbose := false,
    openapiSources := Map.empty,
    openapiResources := Map.empty,
    openapiOutputDir := target.value / "openapi",
    sourceManaged in openapi := sourceManaged.value / "openapi",
    resourceManaged in openapi := resourceManaged.value / "openapi",
    managedSourceDirectories in Compile += (sourceManaged in openapi).value,
    managedResourceDirectories in Compile += (resourceManaged in openapi).value,
    openapiConfigurator := {
      val log = sLog.value
      val configurator = openapiConfigurationFile.value
        .flatMap(file => Option(CodegenConfigurator.fromFile(file.getAbsolutePath)))
        .getOrElse(new CodegenConfigurator())
      configurator.setVerbose(openapiVerbose.value)

      tryNewerFeature(log, "Spec validation is not available in the current OpenAPI generator version.") {
        configurator.setValidateSpec(!openapiSkipValidateSpec.value)
      }

      configurator.setInputSpec(openapiInputSpec.value.getAbsolutePath)
      configurator.setOutputDir(openapiOutputDir.value.getAbsolutePath)

      tryNewerFeature(log, "Alias as model feature is not available in the current OpenAPI generator version.") {
        configurator.setGenerateAliasAsModel(openapiGenerateAliasAsModel.value)
      }

      configurator.setGeneratorName(openapiGeneratorName.value)
      configurator.setApiPackage(openapiApiPackage.value)
      configurator.setModelPackage(openapiModelPackage.value)
      configurator.setInvokerPackage(openapiInvokerPackage.value)
      configurator.setGroupId(Keys.organization.value)
      configurator.setArtifactId(Keys.name.value)
      configurator.setArtifactVersion(Keys.version.value)

      tryNewerFeature(log, "GeneratorProperties is not available in the current OpenAPI generator version.") {
        if (openapiGenerateApis.value) {
          GeneratorProperties.setProperty(CodegenConstants.APIS, "")
        } else {
          GeneratorProperties.clearProperty(CodegenConstants.APIS)
        }

        if (openapiGenerateModels.value) {
          GeneratorProperties.setProperty(CodegenConstants.MODELS, openapiModelsToGenerate.value.mkString(","))
        } else {
          GeneratorProperties.clearProperty(CodegenConstants.MODELS)
        }

        if (openapiGenerateSupportingFiles.value) {
          GeneratorProperties.setProperty(CodegenConstants.SUPPORTING_FILES, openapiSupportingFilesToGenerate.value.mkString(","))
        } else {
          GeneratorProperties.clearProperty(CodegenConstants.SUPPORTING_FILES)
        }

        GeneratorProperties.setProperty(CodegenConstants.MODEL_TESTS, openapiGenerateModelTests.value.toString)
        GeneratorProperties.setProperty(CodegenConstants.MODEL_DOCS, openapiGenerateModelDocumentation.value.toString)
        GeneratorProperties.setProperty(CodegenConstants.API_TESTS, openapiGenerateApiTests.value.toString)
        GeneratorProperties.setProperty(CodegenConstants.API_DOCS, openapiGenerateApiDocumentation.value.toString)
      }

      for ((key, value) <- openapiAdditionalProperties.value) {
        configurator.addAdditionalProperty(key, value)
      }

      configurator
    },
    openapiGenerate := {
      OpenApiTasks.generate(openapiConfigurator.value)
    },
    openapiCopySources := {
      val generatedDir = openapiGenerate.value
      val filesToCopy = openapiSources.value
      val sourcesDir = (sourceManaged in openapi).value
      OpenApiTasks.copyFileList(filesToCopy, generatedDir, sourcesDir, streams.value.log)
    },
    openapiCopyResources := {
      val generatedDir = openapiGenerate.value
      val filesToCopy = openapiResources.value
      val resourcesDir = (resourceManaged in openapi).value
      OpenApiTasks.copyFileList(filesToCopy, generatedDir, resourcesDir, streams.value.log)
    },
    openapi := {
      openapiCopySources.value
      openapiCopyResources.value
    }
  )

  /**
    * The plugin is built against openapi-generator 4.x. This method helps make code generation successful
    * for versions 3.x as well, in case anyone wants to use these for whatever reason
    */
  def tryNewerFeature(log: Logger, failureMessage: String)(action: => Unit): Unit = {
    try {
      action
    } catch {
      case _: NoSuchMethodError | _: NoClassDefFoundError =>
        log.warn(failureMessage)
    }
  }
}
