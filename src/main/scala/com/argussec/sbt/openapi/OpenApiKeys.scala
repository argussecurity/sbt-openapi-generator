package com.argussec.sbt.openapi

import org.openapitools.codegen.config.CodegenConfigurator
import sbt._

object OpenApiKeys {
  val openapi = taskKey[Unit]("Generates files from the OpenAPI spec and arranges them in their destination directories.")
  val openapiGenerate = taskKey[File]("Generates files from the OpenAPI spec.")
  val openapiCopySources = taskKey[Seq[File]]("Arranges the generated sources in the managed sources directory.")
  val openapiCopyResources = taskKey[Seq[File]]("Arranges the generated resources in the managed resources directory.")
  val openapiConfigurator = settingKey[CodegenConfigurator]("The configurator to provide to the OpenAPI code generator.")
  val openapiGeneratorName = settingKey[String]("The name of the generator to use.")
  val openapiInputSpec = settingKey[File]("Location of the OpenAPI spec file.")
  val openapiOutputDir = settingKey[File]("The directory in which the files are generated.")
  val openapiConfigurationFile = settingKey[Option[File]]("A JSON configuration file to use as a base.")
  val openapiApiPackage = settingKey[String]("The package name to use for generated API objects/classes.")
  val openapiModelPackage = settingKey[String]("The package name to use for generated model objects/classes.")
  val openapiInvokerPackage = settingKey[String]("The package name to use for the generated invoker objects.")
  val openapiSkipValidateSpec = settingKey[Boolean]("Whether to skip the OpenAPI spec validation.")
  val openapiGenerateAliasAsModel = settingKey[Boolean]("Whether to generate aliases (array, map) as model.")
  val openapiAdditionalProperties = settingKey[Map[String, String]]("A map of additional properties that can be referenced by the mustache templates.")
  val openapiGenerateApis = settingKey[Boolean]("Whether to generate API files.")
  val openapiGenerateModels = settingKey[Boolean]("Whether to generate model files.")
  val openapiModelsToGenerate = settingKey[List[String]]("A list of models to generate. By default all models are generated.")
  val openapiGenerateSupportingFiles = settingKey[Boolean]("Whether to generate the supporting files.")
  val openapiSupportingFilesToGenerate = settingKey[List[String]]("A list of supporting files to generate. By default all supporting files are generated.")
  val openapiGenerateModelTests = settingKey[Boolean]("Whether to generate the model tests.")
  val openapiGenerateModelDocumentation = settingKey[Boolean]("Whether to generate the model documentation.")
  val openapiGenerateApiTests = settingKey[Boolean]("Whether to generate the API tests.")
  val openapiGenerateApiDocumentation = settingKey[Boolean]("Whether to generate the API documentation.")
  val openapiVerbose = settingKey[Boolean]("Enable verbose output.")
  val openapiSources = settingKey[Map[String, String]]("Mapping of generated files to the managed sources directory.")
  val openapiResources = settingKey[Map[String, String]]("Mapping of generated files to the managed resources directory.")
}
