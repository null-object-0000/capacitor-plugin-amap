import type { Plugin } from '@capacitor/core';
import { registerPlugin } from '@capacitor/core';

import type {
    AMapConfig,
    CameraPosition,
    MapStatusLimits,
    MapType,
    UiSettings
} from './definitions';

export interface CreateMapArgs {
    /**
     * 地图实例的唯一标识符。
     * @since 0.0.1
     */
    id: string;
    /**
     * 地图的初始配置设置。
     * @since 0.0.1
     */
    config: AMapConfig;
    /**
     * The DOM element that the Google Map View will be mounted on which determines size and positioning.
     * @since 0.0.1
     */
    element: HTMLElement;
    /**
     * 如果已经存在具有提供的`id`的地图，则销毁并重新创建地图实例。
     * @default false
     * @since 0.0.1
     */
    forceCreate?: boolean;
}

/**
 * 定位蓝点展现模式。
 * @since 0.0.5
 */
export enum MyLocationType {
    /**
     * 只定位一次。
     */
    LOCATION_TYPE_SHOW = 0,
    /**
     * 定位一次，且将视角移动到地图中心点。
     */
    LOCATION_TYPE_LOCATE = 1,
    /**
     * 连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
     */
    LOCATION_TYPE_FOLLOW = 2,
    /**
     * 连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
     */
    LOCATION_TYPE_MAP_ROTATE = 3,
    /**
     * 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
     */
    LOCATION_TYPE_LOCATION_ROTATE = 4,
    /**
     * 连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
     */
    LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER = 5,
    /**
     * 连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
     */
    LOCATION_TYPE_FOLLOW_NO_CENTER = 6,
    /**
     * 连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
     */
    LOCATION_TYPE_MAP_ROTATE_NO_CENTER = 7,
}

/**
 * @since 0.0.5
 */
export interface MyLocationStyle {
    /**
     * 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生
     */
    interval: number;
    /**
     * 设置定位蓝点展现模式。
     */
    myLocationType: MyLocationType;
    /**
     * 设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
     */
    showMyLocation: boolean;
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

/**
 * 地图内置UI及手势控制器。
 * @since 0.0.5
 */
export interface SetUiSettingsArgs extends UiSettings {
    /**
     * 地图实例的唯一标识符。
     */
    id: string;
}

/**
 * 通过指定的两个经纬度坐标（左下、右上）构建的一个矩形区域
 * @since 0.0.6
 */
export interface SetMapStatusLimitsArgs extends MapStatusLimits {
    /**
     * 地图实例的唯一标识符。
     */
    id: string;
}

export interface AMapPlugin extends Plugin {
    updatePrivacyShow(args: { isContains: boolean; isShow: boolean; }): Promise<void>;
    updatePrivacyAgree(args: { isAgree: boolean; }): Promise<void>;
    setTerrainEnable(args: { isTerrainEnable: boolean; }): Promise<void>;
    create(options: CreateMapArgs): Promise<void>;
    showIndoorMap(args: { id: string; enable: boolean; }): Promise<void>;
    setMapType(args: { id: string; type: MapType; }): Promise<void>;
    setTrafficEnabled(args: { id: string; enable: boolean; }): Promise<void>;
    destroy(args: { id: string }): Promise<void>;
    enableTouch(args: { id: string }): Promise<void>;
    disableTouch(args: { id: string }): Promise<void>;
    onScroll(args: MapBoundsArgs): Promise<void>;
    onResize(args: MapBoundsArgs): Promise<void>;
    onDisplay(args: MapBoundsArgs): Promise<void>;
    dispatchMapEvent(args: { id: string; focus: boolean }): Promise<void>;

    enableMyLocation(args: { id: string; }): Promise<void>;
    disableMyLocation(args: { id: string; }): Promise<void>;
    setMyLocationStyle(args: { id: string; style: MyLocationStyle; }): Promise<void>;

    setUiSettings(args: SetUiSettingsArgs): Promise<void>;

    cameraUpdatePosition(args: { id: string; cameraOptions: CameraPosition; }): Promise<void>;
    cameraZoomTo(args: { id: string; zoom: Number; }): Promise<void>;
    setMapStatusLimits(args: SetMapStatusLimitsArgs): Promise<void>;
}


const CapacitorAMap = registerPlugin<AMapPlugin>('CapacitorAMap');

CapacitorAMap.addListener('isMapInFocus', data => {
    const x = data.x;
    const y = data.y;

    const elem = document.elementFromPoint(x, y) as HTMLElement | null;
    const internalId = elem?.dataset?.internalId;
    const mapInFocus = internalId === data.mapId;

    CapacitorAMap.dispatchMapEvent({ id: data.mapId, focus: mapInFocus });
});

export { CapacitorAMap };