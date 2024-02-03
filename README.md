# @snewbie/capacitor-amap

<a href="https://www.npmjs.com/package/@snewbie/capacitor-amap"><img src="https://img.shields.io/npm/v/@snewbie/capacitor-amap.svg?sanitize=true" alt="Version"></a> <a href="https://www.npmjs.com/package/@snewbie/capacitor-amap"><img src="https://img.shields.io/npm/l/@snewbie/capacitor-amap.svg?sanitize=true" alt="License"></a>

使用 [高德开放平台 Android 地图 SDK](https://lbs.amap.com/api/android-sdk/gettingstarted) 开发的 Capacitor 插件（安卓端）。

## Install

```bash
npm install @snewbie/capacitor-amap
npx cap sync
```

## API Keys

要在安卓平台上使用高德地图 SDK，需要申请一个 API Key。请参考 [高德地图 SDK 文档](https://lbs.amap.com/api/android-sdk/guide/create-project/get-key) 。

## Android（详细配置说明请参考 [高德地图 SDK 文档](https://lbs.amap.com/api/android-sdk/guide/create-project/dev-attention)）

Android 版高德地图 SDK 要求您将 API 密钥添加到项目中的 AndroidManifest.xml 文件中。

```xml
<meta-data android:name="com.amap.api.v2.apikey" android:value="请输入您的用户 Key"/>
```

要使用某些功能，还需要将以下权限添加到项目中的 AndroidManifest.xml 文件中：

```xml
<!--允许访问网络，必选权限-->
<uses-permission android:name="android.permission.INTERNET" />  

<!--允许获取粗略位置，若用GPS实现定位小蓝点功能则必选-->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> 

<!--允许获取网络状态，用于网络定位，若无gps但仍需实现定位小蓝点功能则此权限必选-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />    

<!--允许获取wifi网络信息，用于网络定位，若无gps但仍需实现定位小蓝点功能则此权限必选-->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> 

<!--允许获取wifi状态改变，用于网络定位，若无gps但仍需实现定位小蓝点功能则此权限必选-->
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" /> 

<!--允许写入扩展存储，用于数据缓存，若无此权限则写到私有目录-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 

<!--允许写设备缓存，用于问题排查-->
<uses-permission android:name="android.permission.WRITE_SETTINGS" />  

<!--允许读设备等信息，用于问题排查-->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
```

## Usage

> 在 Android 上，地图在整个网络视图下呈现，并使用该组件在滚动事件期间管理其位置。这意味着，作为开发人员，您必须确保 Web 视图在所有层到最底层都是透明的。在典型的 Ionic 应用程序中，这意味着对 IonContent 和根 HTML 标记等元素设置透明度，以确保它可以被看到。如果你在 Android 上看不到你的地图，这应该是你检查的第一件事。

高德地图元素本身没有样式，所以你应该根据页面结构的布局对其进行样式设置。因为我们将视图渲染到这个槽中，所以元素本身没有宽度或高度，所以一定要明确设置这些宽度或高度。

```html
<div id="map" class="capacitor-map"></div>
```

```css
.capacitor-map {
  display: inline-block;
  width: 275px;
  height: 400px;
}
```

接下来，我们应该创建地图引用。这是通过从 Capacitor 插件导入 AMap 类并调用 create 方法，然后传入所需的参数来完成的。

```typescript
import { AMap } from '@snewbie/capacitor-amap';

const mapRef = document.getElementById('map');

const newMap = await AMap.create({
  id: 'my-map', // Unique identifier for this map instance
  element: mapRef, // reference to the capacitor-google-map element
  config: {
    center: {
      // The initial position to be rendered by the map
      lat: 33.6,
      lng: -117.9,
    },
    zoom: 8, // The initial zoom level to be rendered by the map
  },
});
```

## Full Examples

### Vue

```html
<template>
    <div id="map" ref="mapRef" :style="{ display: 'inline-block', width: '275px', height: '400px' }"></div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';
import { AMap } from '@snewbie/capacitor-amap';

const mapRef = ref<HTMLElement | null>(null)
let newMap: Amap;

onMounted(async () => {
    if (!mapRef.value) { return; }

    newMap = await AMap.create({
        id: 'main',
        element: mapRef.value, 
        config: {
            center: {
                // The initial position to be rendered by the map
                lat: 33.6,
                lng: -117.9,
            },
            zoom: 8, // The initial zoom level to be rendered by the map
        },
    });
});

onUnmounted(() => {
  if (map) {
    map.destroy()
  }
})
</script>
```

## API

<docgen-index>

* [`updatePrivacyShow(...)`](#updateprivacyshow)
* [`updatePrivacyAgree(...)`](#updateprivacyagree)
* [`create(...)`](#create)
* [`destroy()`](#destroy)
* [`enableTouch()`](#enabletouch)
* [`disableTouch()`](#disabletouch)
* [`setOnCameraChangeListener(...)`](#setoncamerachangelistener)
* [`setOnIndoorBuildingActiveListener(...)`](#setonindoorbuildingactivelistener)
* [`setOnInfoWindowClickListener(...)`](#setoninfowindowclicklistener)
* [`setOnMapClickListener(...)`](#setonmapclicklistener)
* [`setOnMapLoadedListener(...)`](#setonmaploadedlistener)
* [`setOnMapLongClickListener(...)`](#setonmaplongclicklistener)
* [`setOnMapTouchListener(...)`](#setonmaptouchlistener)
* [`setOnMarkerClickListener(...)`](#setonmarkerclicklistener)
* [`setOnMarkerDragListener(...)`](#setonmarkerdraglistener)
* [`setOnMultiPointClickListener(...)`](#setonmultipointclicklistener)
* [`setOnMyLocationChangeListener(...)`](#setonmylocationchangelistener)
* [`setOnPOIClickListener(...)`](#setonpoiclicklistener)
* [`setOnPolylineClickListener(...)`](#setonpolylineclicklistener)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

高德地图 SDK 的 JavaScript 接口。

### updatePrivacyShow(...)

```typescript
updatePrivacyShow(isContains: boolean, isShow: boolean) => Promise<void>
```

更新隐私合规状态，需要在初始化地图之前完成

| Param            | Type                 | Description                |
| ---------------- | -------------------- | -------------------------- |
| **`isContains`** | <code>boolean</code> | 隐私权政策是否包含高德开平隐私权政策 true是包含 |
| **`isShow`**     | <code>boolean</code> | 隐私权政策是否弹窗展示告知用户 true是展示    |

--------------------


### updatePrivacyAgree(...)

```typescript
updatePrivacyAgree(isAgree: boolean) => Promise<void>
```

更新同意隐私状态，需要在初始化地图之前完成

| Param         | Type                 | Description             |
| ------------- | -------------------- | ----------------------- |
| **`isAgree`** | <code>boolean</code> | 隐私权政策是否取得用户同意 true是用户同意 |

--------------------


### create(...)

```typescript
create(options: CreateMapArgs, callback?: MapListenerCallback<MapReadyCallbackData> | undefined) => Promise<AMap>
```

创建地图实例。

| Param          | Type                                                                                                                                | Description |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **`options`**  | <code><a href="#createmapargs">CreateMapArgs</a></code>                                                                             | - 创建地图的参数。  |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;<a href="#mapreadycallbackdata">MapReadyCallbackData</a>&gt;</code> |             |

**Returns:** <code>Promise&lt;AMap&gt;</code>

--------------------


### destroy()

```typescript
destroy() => Promise<void>
```

销毁地图实例。

--------------------


### enableTouch()

```typescript
enableTouch() => Promise<void>
```

设置地图允许被触控。

--------------------


### disableTouch()

```typescript
disableTouch() => Promise<void>
```

设置地图禁止被触控。

--------------------


### setOnCameraChangeListener(...)

```typescript
setOnCameraChangeListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置地图状态的监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnIndoorBuildingActiveListener(...)

```typescript
setOnIndoorBuildingActiveListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置室内地图状态监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnInfoWindowClickListener(...)

```typescript
setOnInfoWindowClickListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置marker的信息窗口点击事件监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMapClickListener(...)

```typescript
setOnMapClickListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置地图点击事件监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMapLoadedListener(...)

```typescript
setOnMapLoadedListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置地图加载完成监听接口

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMapLongClickListener(...)

```typescript
setOnMapLongClickListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置地图长按事件监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMapTouchListener(...)

```typescript
setOnMapTouchListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置地图触摸事件监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMarkerClickListener(...)

```typescript
setOnMarkerClickListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置marker点击事件监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMarkerDragListener(...)

```typescript
setOnMarkerDragListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

marker拖动事件监听接口

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMultiPointClickListener(...)

```typescript
setOnMultiPointClickListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置海量点单击事件监听

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnMyLocationChangeListener(...)

```typescript
setOnMyLocationChangeListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置用户定位信息监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnPOIClickListener(...)

```typescript
setOnPOIClickListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置底图poi点击事件监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### setOnPolylineClickListener(...)

```typescript
setOnPolylineClickListener(callback?: MapListenerCallback<any> | undefined) => Promise<void>
```

设置polyline点击事件监听接口。

| Param          | Type                                                                           |
| -------------- | ------------------------------------------------------------------------------ |
| **`callback`** | <code><a href="#maplistenercallback">MapListenerCallback</a>&lt;any&gt;</code> |

--------------------


### Interfaces


#### CreateMapArgs

| Prop              | Type                                              | Description                                                                                        | Default            |
| ----------------- | ------------------------------------------------- | -------------------------------------------------------------------------------------------------- | ------------------ |
| **`id`**          | <code>string</code>                               | 地图实例的唯一标识符。                                                                                        |                    |
| **`config`**      | <code><a href="#amapconfig">AMapConfig</a></code> | 地图的初始配置设置。                                                                                         |                    |
| **`element`**     | <code>HTMLElement</code>                          | The DOM element that the Google Map View will be mounted on which determines size and positioning. |                    |
| **`forceCreate`** | <code>boolean</code>                              | 如果已经存在具有提供的`id`的地图，则销毁并重新创建地图实例。                                                                   | <code>false</code> |


#### AMapConfig

| Prop                   | Type                 | Description                                                                                         | Default           |
| ---------------------- | -------------------- | --------------------------------------------------------------------------------------------------- | ----------------- |
| **`width`**            | <code>number</code>  | Override width for native map.                                                                      |                   |
| **`height`**           | <code>number</code>  | Override height for native map.                                                                     |                   |
| **`x`**                | <code>number</code>  | Override absolute x coordinate position for native map.                                             |                   |
| **`y`**                | <code>number</code>  | Override absolute y coordinate position for native map.                                             |                   |
| **`devicePixelRatio`** | <code>number</code>  | Override pixel ratio for native map.                                                                |                   |
| **`touchPoiEnable`**   | <code>boolean</code> | 设置地图`POI`是否允许点击。 默认情况下单击地铁站，地铁路线会高亮，如果关闭了poi单击，则地铁站不会被单击，地铁路线也不会高亮 - true 表示允许点击，为默认值 - false 不允许点击 | <code>true</code> |


#### MapReadyCallbackData

| Prop        | Type                |
| ----------- | ------------------- |
| **`mapId`** | <code>string</code> |


### Type Aliases


#### MapListenerCallback

The callback function to be called when map events are emitted.

<code>(data: T): void</code>

</docgen-api>
