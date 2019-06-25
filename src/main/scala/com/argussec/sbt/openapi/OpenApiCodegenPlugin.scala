package com.argussec.sbt.openapi

import sbt._

object OpenApiCodegenPlugin extends AutoPlugin {

  object autoImport {
    val OpenApiKeys = com.argussec.sbt.openapi.OpenApiKeys
    val openapi = OpenApiKeys.openapi
    val openapiGeneratorName = OpenApiKeys.openapiGeneratorName
    val openapiInputSpec = OpenApiKeys.openapiInputSpec
    val openapiSkipValidateSpec = OpenApiKeys.openapiSkipValidateSpec
    val openapiModelPackage = OpenApiKeys.openapiModelPackage
    val openapiApiPackage = OpenApiKeys.openapiApiPackage
    val openapiAdditionalProperties = OpenApiKeys.openapiAdditionalProperties
    val openapiSources = OpenApiKeys.openapiSources
    val openapiResources = OpenApiKeys.openapiResources
  }

  override lazy val projectSettings = OpenApiSettings.baseOpenApiSettings
}
