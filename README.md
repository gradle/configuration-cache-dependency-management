# configuration-cache-dependency-management

Samples to demonstrate improvements to dependency management support with configuration cache

You will need the `gpg` command installed.

## Dynamic versions with Ivy and Maven repositories

```shell
> ./gradlew publish --no-configuration-cache 
> ./gradlew resolve
> ./gradlew resolve
> ./gradle publish --no-configuration-cache -DlibVersion=2.0
```

## Dependency verification
