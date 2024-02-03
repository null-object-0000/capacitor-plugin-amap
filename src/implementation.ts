import type { Plugin } from '@capacitor/core';
import { registerPlugin } from '@capacitor/core';

import type {
    AMapConfig
} from './definitions';


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
    updatePrivacyShow(arg0: { isContains: boolean; isShow: boolean; }): Promise<void>;
    updatePrivacyAgree(arg0: { isAgree: boolean; }): Promise<void>;
    create(options: CreateMapArgs): Promise<void>;
    destroy(args: { id: string }): Promise<void>;
    enableTouch(args: { id: string }): Promise<void>;
    disableTouch(args: { id: string }): Promise<void>;
    onScroll(args: MapBoundsArgs): Promise<void>;
    onResize(args: MapBoundsArgs): Promise<void>;
    onDisplay(args: MapBoundsArgs): Promise<void>;
    dispatchMapEvent(args: { id: string; focus: boolean }): Promise<void>;
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