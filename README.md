# sbt-openapi-generator

This project is an SBT plugin which invokes [openapi-generator]. After code generation, the plugin copies only the
relevant files (according to the configuration) to the managed sourceset. This behavior allows one to use only some of 
the generated files and reorganize them if needed.

## Usage

Add the following line to `project/plugins.sbt`:

```scala
addSbtPlugin("com.argus-sec" % "sbt-openapi-generator" % "0.1.0")
```

Enable and configure the plugin in your `build.sbt`:

```scala
enablePlugins(OpenApiCodegenPlugin)

openapiGeneratorName := "<generator-name>"
openapiInputSpec := file("path/to/openapi.yaml")
openapiSources := Map("src/main/scala" -> "")
openapiResources := Map("src/main/resources" -> "")
```

## Configuration

All setting and task keys are defined under `com.argussec.sbt.openapi.OpenApiKeys`, which is auto-imported as
`OpenApiKeys`. The available keys are:

|Name                             |Description                                                                           |Type                 |Default                                   |Auto-imported?|
|---------------------------------|--------------------------------------------------------------------------------------|---------------------|------------------------------------------|--------------|
|openapi                          |Invokes openapi-generator and copies the relevant files to the managed sourceset.     |`Unit`               |(task)                                    |Yes           |
|openapiGeneratorName             |The name of the generator to use.                                                     |`String`             |(unset)                                   |Yes           |
|openapiInputSpec                 |Location of the OpenAPI spec file.                                                    |`File`               |(unset)                                   |Yes           |
|openapiSkipValidateSpec          |Whether to skip the OpenAPI spec validation.                                          |`Boolean`            |`false`                                   |Yes           |
|openapiModelPackage              |The package name to use for generated model objects/classes.                          |`String`             |`<organization>.openapi.generated.model`  |Yes           |
|openapiApiPackage                |The package name to use for generated API objects/classes.                            |`String`             |`<organization>.openapi.generated.api`    |Yes           |
|openapiAdditionalProperties      |A map of additional properties that can be referenced by the mustache templates.      |`Map[String, String]`|`Map.empty`                               |Yes           |
|openapiSources                   |Mapping of generated files to the managed sources directory.                          |`Map[String, String]`|`Map.empty`                               |Yes           |
|openapiResources                 |Mapping of generated files to the managed resources directory.                        |`Map[String, String]`|`Map.empty`                               |Yes           |
|openapiGenerate                  |Generates files from the OpenAPI spec.                                                |`File`               |(task)                                    |No            |                             
|openapiCopySources               |Arranges the generated sources in the managed sources directory.                      |`Seq[File]`          |(task)                                    |No            |
|openapiCopyResources             |Arranges the generated resources in the managed resources directory.                  |`Seq[File]`          |(task)                                    |No            |
|openapiConfigurator              |The configurator to provide to the OpenAPI code generator.                            |`CodegenConfigurator`|(built upon other settings)               |No            |
|openapiOutputDir                 |The directory in which the files are generated.                                       |`File`               |`<target>/openapi`                        |No            |
|openapiConfigurationFile         |A JSON configuration file to use as a base.                                           |`Option[File]`       |`None`                                    |No            |
|openapiInvokerPackage            |The package name to use for the generated invoker objects.                            |`String`             |`<organization>.openapi.generated.invoker`|No            |
|openapiGenerateAliasAsModel      |Whether to generate aliases (array, map) as model.                                    |`Boolean`            |`false`                                   |No            |
|openapiGenerateApis              |Whether to generate API files.                                                        |`Boolean`            |`true`                                    |No            |
|openapiGenerateModels            |Whether to generate model files.                                                      |`Boolean`            |`true`                                    |No            |
|openapiModelsToGenerate          |A list of models to generate. By default all models are generated.                    |`List[String]`       |`List.empty`                              |No            |
|openapiGenerateSupportingFiles   |Whether to generate the supporting files.                                             |`Boolean`            |`true`                                    |No            |
|openapiSupportingFilesToGenerate |A list of supporting files to generate. By default all supporting files are generated.|`List[String]`       |`List.empty`                              |No            |
|openapiGenerateModelTests        |Whether to generate the model tests.                                                  |`Boolean`            |`true`                                    |No            |
|openapiGenerateModelDocumentation|Whether to generate the model documentation.                                          |`Boolean`            |`true`                                    |No            |
|openapiGenerateApiTests          |Whether to generate the API tests.                                                    |`Boolean`            |`true`                                    |No            |
|openapiGenerateApiDocumentation  |Whether to generate the API documentation.                                            |`Boolean`            |`true`                                    |No            |
|openapiVerbose                   |Whether to enable verbose output.                                                     |`Boolean`            |`false`                                   |No            |

## Controlling which files are compiled

`openapiSources` and `openapiResources` are setting keys of type `Map[String, String]` which are used to determine the
source and destination of managed sources and resources respectively; the keys determine the path in the generated
files, and the corresponding values determine where they're placed in the managed sourceset. By default, no files are 
copied and thus the generated files do not participate in the compilation process.

Note that the source root of the managed sourceset is its root directory (i.e. `target/scala-2.x/src_managed/openapi` 
and `target/scala-2.x/resource_managed/openapi`). When specifying generated files you should start from the source root
of the generated files (e.g. `src/main/scala`).

**Example** - add all generated sources under `src/main/scala` and all generated resources under `src/main/resources`
to the managed sourceset:

```scala
openapiSources := Map("src/main/scala" -> "")
openapiResources := Map("src/main/resources" -> "")
```

**Example** - adding a specific package:

```scala
openapiSources := Map("src/main/scala/com/example/api" -> "com/example/api")
```

## Using a different version of openapi-generator

This plugin depends on openapi-generator version 4.0.2. Over time, bug fixes and new features are introduced to
openapi-generator. Since the code generation API rarely changes, it makes little sense to provide a new version of this
plugin for every new release of openapi-generator; however, it is possible to use a newer openapi-generator, assuming 
there are no drastic changes to the code generation API. Older versions are supported as well (tested against 3.0.0).

To use another version of openapi-generator, add the following line to `project/plugins.sbt`:

```scala
dependencyOverrides += "org.openapitools" % "openapi-generator" % "<version>"
```

## License

Licensed under Apache 2.0 license. Consult LICENSE file for details.

[openapi-generator]: https://github.com/OpenAPITools/openapi-generator