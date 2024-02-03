export interface AMapConfig {
  /**
   * Override width for native map.
   */
  width?: number;
  /**
   * Override height for native map.
   */
  height?: number;
  /**
   * Override absolute x coordinate position for native map.
   */
  x?: number;
  /**
   * Override absolute y coordinate position for native map.
   */
  y?: number;
  /**
   * Override pixel ratio for native map.
   */
  devicePixelRatio?: number;

  /**
   * 设置地图`POI`是否允许点击。
   * 默认情况下单击地铁站，地铁路线会高亮，如果关闭了poi单击，则地铁站不会被单击，地铁路线也不会高亮
   *  - true 表示允许点击，为默认值
   *  - false 不允许点击
   * @default true
   */
  touchPoiEnable?: boolean;
}

/**
 * The callback function to be called when map events are emitted.
 */
export type MapListenerCallback<T> = (data: T) => void;

export interface MapReadyCallbackData {
  mapId: string;
}