# configuration-cache-dependency-management

Samples to demonstrate improvements to dependency management support with configuration cache

You will need the `gpg` command installed to sign the published artifacts.

## Signing plugin

The signing plugin is now configuration cache compatible.

To try this out, use the `publish` task from the [maven-lib](maven-lib/build.gradle.kts) project. This will
publish a Jar and metadata to `maven-lib/build/repo/maven` along with signature files.

```shell
> ./gradlew maven-lib:publish
> ./gradlew clean
> ./gradlew maven-lib:publish
```

```shell
> alias gradle76=# point this at a Gradle 7.6 installation
> gradle76 maven-lib:publish
```

## Dynamic versions with Ivy and Maven repositories

```shell
> ./gradlew publish --no-configuration-cache
> ./gradlew resolve
> ./gradlew resolve
> ./gradle ivy-lib:publish --no-configuration-cache -DlibVersion=2.0
> ./gradlew resolve
```

```shell
> gradle76 resolve
> ./gradle ivy-lib:publish --no-configuration-cache -DlibVersion=3.0
> gradle76 resolve
```

## Dependency verification
