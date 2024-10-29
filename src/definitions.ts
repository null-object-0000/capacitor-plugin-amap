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
 * 相机位置，这个类包含了所有的可视区域的位置参数。
 * @since 0.0.6
 */
export interface CameraPosition {
  /**
   * 目标位置的屏幕中心点经纬度坐标。
   */
  target: LatLng;
  /**
   * 目标可视区域的缩放级别。
   */
  zoom: number;
  /**
   * 目标可视区域的倾斜度，以角度为单位。
   */
  tilt: number;
  /**
   * 可视区域指向的方向，以角度为单位，从正北向逆时针方向计算，从0 度到360 度。
   */
  bearing: number;
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

  /**
   * 设置地图初始化时的地图状态， 默认地图中心点为北京天安门，缩放级别为 10.0f。
   */
  cameraOptions?: CameraPosition;
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
 * 存储经纬度坐标值的类，单位角度。
 * @since 0.0.6
 */
export interface LatLng {
  /**
   * 纬度 (垂直方向)
   */
  latitude: number;
  /**
   * 经度 (水平方向)
   */
  longitude: number;
}

/**
 * 通过指定的两个经纬度坐标（左下、右上）构建的一个矩形区域
 * @since 0.0.6
 */
export interface MapStatusLimits {
  /**
   * 西南角坐标。
   */
  southwest: LatLng;
  /**
   * 东北角坐标。
   */
  northeast: LatLng;
}

/**
 * The callback function to be called when map events are emitted.
 * @since 0.0.1
 */
export type MapListenerCallback<T> = (data: T) => void;

export interface MapReadyCallbackData {
  mapId: string;
}

/**
 * @since 0.0.8
 */
export interface GetFromLocationArgs {
  /**
   * 经纬度坐标，经纬度小数点后不要超过 6 位。
   */
  location: LatLng;
  /**
   * 搜索半径，取值范围：0~3000，单位：米。
   * @default 1000
   */
  radius: number;
}