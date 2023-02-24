# shared-prefs-explorer

[![](https://jitpack.io/v/uk.co.conjure/shared-prefs-explorer.svg)](https://jitpack.io/#uk.co.conjure/shared-prefs-explorer)


## Add it to your project

**Step 1** add repository

Add `jitpack.io` to your root project build.gradle file:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

```

**Step 2** add the dependency

```
dependencies {
  debugImplementation 'uk.co.conjure:shared-prefs-explorer:<TAG>'
}
```

Note that we recommend adding the explorere as `debugImplementation` dependency. This way it will only be added to your `debug` build.

## Starting the Explorer via ADB

You can start the Activity from your Terminal by replacing `com.example.project` with your projects package name.

```
adb shell am start com.example.project/uk.co.conjure.sharedprefsexplorer.PreferenceBrowserActivity
```

## Starting the Explorer in Code

If you want to start the Explorer programmatically e.g. by adding a button you can do so via
```
import uk.co.conjure.sharedprefsexplorer.PreferenceBrowserActivity
//...
 
 context.startActivity(Intent(context, PreferenceBrowserActivity::class.java))
 
```

If you added the dependency as `debugImplementation` this Activity will not be available in your release code. A nice way to get around this is demonstrated in the `app` folder.
By creating the same `SharedPrefsExplorerProxy` class in both the `debug` and `release` folder the call to its `start` function is a noop in production while starting the browser in your debug build.
