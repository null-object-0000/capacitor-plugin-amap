# capacitor-plugin-amap

Capacitor Plugin using native AMap SDK for Android.

## Install

```bash
npm install capacitor-plugin-amap
npx cap sync
```

## API

<docgen-index>

* [`create(...)`](#create)
* [`destroy()`](#destroy)
* [`enableTouch()`](#enabletouch)
* [`disableTouch()`](#disabletouch)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

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
