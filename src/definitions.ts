import type { Plugin } from '@capacitor/core';

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

export interface CreateMapArgs {
  /**
   * 地图实例的唯一标识符。
   */
  id: string;
  /**
   * 地图的初始配置设置。
   */
  config: AMapConfig;
  /**
   * The DOM element that the Google Map View will be mounted on which determines size and positioning.
   */
  element: HTMLElement;
  /**
   * 如果已经存在具有提供的`id`的地图，则销毁并重新创建地图实例。
   * @default false
   */
  forceCreate?: boolean;
}

export interface MapBoundsArgs {
  id: string;
  mapBounds: {
    x: number;
    y: number;
    width: number;
    height: number;
  };
}

export interface AMapPlugin extends Plugin {
  create(options: CreateMapArgs): Promise<void>;
  destroy(args: { id: string }): Promise<void>;
  enableTouch(args: { id: string }): Promise<void>;
  disableTouch(args: { id: string }): Promise<void>;
  onScroll(args: MapBoundsArgs): Promise<void>;
  onResize(args: MapBoundsArgs): Promise<void>;
  onDisplay(args: MapBoundsArgs): Promise<void>;
  dispatchMapEvent(args: { id: string; focus: boolean }): Promise<void>;
}
