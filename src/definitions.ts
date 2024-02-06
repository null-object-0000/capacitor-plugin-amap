/**
 * Logo的位置。
 * @since 0.0.5
 */
export enum LogoPosition {
  /**
   * 左下
   */
  LOGO_POSITION_BOTTOM_LEFT = 0,
  /**
   * 底部居中
   */
  LOGO_POSITION_BOTTOM_CENTER = 1,
  /**
   * 右下
   */
  LOGO_POSITION_BOTTOM_RIGHT = 2
}

/**
 * 地图模式。
 * @since 0.0.5
 */
export enum MapType {
  /**
   * 普通地图
   */
  MAP_TYPE_NORMAL = 1,
  /**
   * 卫星地图
   */
  MAP_TYPE_SATELLITE = 2,
  /**
   * 黑夜地图
   */
  MAP_TYPE_NIGHT = 3,
  /**
   * 导航地图
   */
  MAP_TYPE_NAVI = 4,
  /**
   * 公交地图
   */
  MAP_TYPE_BUS = 5,
  /**
   * 导航黑夜地图
   */
  MAP_TYPE_NAVI_NIGHT = 6
}

/**
 * @since 0.0.1
 */
export interface AMapConfig {
  /**
   * Override width for native map.
   * @since 0.0.1
   */
  width?: number;
  /**
   * Override height for native map.
   * @since 0.0.1
   */
  height?: number;
  /**
   * Override absolute x coordinate position for native map.
   * @since 0.0.1
   */
  x?: number;
  /**
   * Override absolute y coordinate position for native map.
   * @since 0.0.1
   */
  y?: number;
  /**
   * Override pixel ratio for native map.
   * @default 1.00f
   * @since 0.0.1
   */
  devicePixelRatio?: number;
  /**
   * 设置“高德地图”Logo的位置。
   * @default LOGO_POSITION_BOTTOM_LEFT
   * @since 0.0.5
   */
  logoPosition?: LogoPosition;
  /**
   * 设置地图模式，默认普通地图。
   * @default MAP_TYPE_NORMAL
   * @since 0.0.5
   */
  mapType?: MapType;
  /**
   * 设置地图是否显示比例尺，默认为false。
   * @default false
   * @since 0.0.5
   */
  scaleControlsEnabled?: boolean;
  /**
   * 设置地图是否允许缩放。默认为true。
   * @default true
   * @since 0.0.5
   */
  zoomControlsEnabled?: boolean;
  /**
   * 设置指南针是否可用。默认为启用。
   * @default true
   * @since 0.0.5
   */
  compassEnabled?: boolean;
  /**
   * 设置地图是否可以手势滑动。默认为true。
   * @default true
   * @since 0.0.5
   */
  scrollGesturesEnabled?: boolean;
  /**
   * 设置地图是否可以通过手势进行缩放。默认为true。
   * @default true
   * @since 0.0.5
   */
  zoomGesturesEnabled?: boolean;
  /**
   * 设置地图是否可以通过手势倾斜（3D效果），默认为true。
   * @default true
   * @since 0.0.5
   */
  tiltGesturesEnabled?: boolean;
  /**
   * 设置地图是否可以通过手势进行旋转。默认为true.
   * @default true
   * @since 0.0.5
   */
  rotateGesturesEnabled?: boolean;
}

/**
 * 地图内置UI及手势控制器。
 * @since 0.0.5
 */
export interface UiSettings {
  /**
   * 设置定位按钮是否可见。
   * @default false
   */
  myLocationButtonEnabled?: boolean;
}

/**
 * The callback function to be called when map events are emitted.
 * @since 0.0.1
 */
export type MapListenerCallback<T> = (data: T) => void;

export interface MapReadyCallbackData {
  mapId: string;
}