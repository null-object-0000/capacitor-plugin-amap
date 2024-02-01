import { Capacitor } from '@capacitor/core';
import { registerPlugin } from '@capacitor/core';

import type { AMapPlugin, CreateMapArgs } from './definitions';

const CapacitorAMap = registerPlugin<AMapPlugin>('CapacitorAMap');
CapacitorAMap.addListener('isMapInFocus', data => {
    const x = data.x;
    const y = data.y;

    const elem = document.elementFromPoint(x, y) as HTMLElement | null;
    const internalId = elem?.dataset?.internalId;
    const mapInFocus = internalId === data.mapId;

    CapacitorAMap.dispatchMapEvent({ id: data.mapId, focus: mapInFocus });
});

/**
 * The callback function to be called when map events are emitted.
 */
export type MapListenerCallback<T> = (data: T) => void;

export interface MapReadyCallbackData {
    mapId: string;
}

export interface PluginListenerHandle {
    remove: () => Promise<void>;
}

/**
 * @class AMap
 */
export interface AMapInterface {
    /**
     * 创建地图实例。
     * @function AMap.create
     * @param {CreateMapArgs} options - 创建地图的参数。
     * @param callback
     * @returns AMap
     */
    create(options: CreateMapArgs, callback?: MapListenerCallback<MapReadyCallbackData>): Promise<AMap>;
    /**
     * 销毁地图实例。
     */
    destroy(): Promise<void>;
    /**
     * 设置地图允许被触控。
     */
    enableTouch(): Promise<void>;
    /**
     * 设置地图禁止被触控。
     */
    disableTouch(): Promise<void>;
}

export * from './definitions';
export class AMap {
    /**
     * 地图实例的唯一标识符。
     */
    private id: string;
    private element: HTMLElement | null = null;
    private resizeObserver: ResizeObserver | null = null;

    private onBoundsChangedListener?: PluginListenerHandle;
    private onCameraIdleListener?: PluginListenerHandle;
    private onCameraMoveStartedListener?: PluginListenerHandle;
    private onClusterClickListener?: PluginListenerHandle;
    private onClusterInfoWindowClickListener?: PluginListenerHandle;
    private onInfoWindowClickListener?: PluginListenerHandle;
    private onMapClickListener?: PluginListenerHandle;
    private onPolylineClickListener?: PluginListenerHandle;
    private onMarkerClickListener?: PluginListenerHandle;
    private onPolygonClickListener?: PluginListenerHandle;
    private onCircleClickListener?: PluginListenerHandle;
    private onMarkerDragStartListener?: PluginListenerHandle;
    private onMarkerDragListener?: PluginListenerHandle;
    private onMarkerDragEndListener?: PluginListenerHandle;
    private onMyLocationButtonClickListener?: PluginListenerHandle;
    private onMyLocationClickListener?: PluginListenerHandle;

    private constructor(id: string) {
        this.id = id;
    }

    public static async create(options: CreateMapArgs, callback?: MapListenerCallback<MapReadyCallbackData>): Promise<AMap> {
        const newMap = new AMap(options.id);

        if (!options.element) {
            throw new Error('container element is required');
        }

        newMap.element = options.element;
        newMap.element.dataset.internalId = options.id;

        const elementBounds = await AMap.getElementBounds(options.element);
        options.config.width = elementBounds.width;
        options.config.height = elementBounds.height;
        options.config.x = elementBounds.x;
        options.config.y = elementBounds.y;
        options.config.devicePixelRatio = window.devicePixelRatio;

        if (Capacitor.getPlatform() == 'android') {
            newMap.initScrolling();
        }

        if (Capacitor.isNativePlatform()) {
            (options.element as any) = {};

            const getMapBounds = () => {
                const mapRect =
                    newMap.element?.getBoundingClientRect() ?? ({} as DOMRect);
                return {
                    x: mapRect.x,
                    y: mapRect.y,
                    width: mapRect.width,
                    height: mapRect.height,
                };
            };

            const onDisplay = () => {
                CapacitorAMap.onDisplay({
                    id: newMap.id,
                    mapBounds: getMapBounds(),
                });
            };

            const onResize = () => {
                CapacitorAMap.onResize({
                    id: newMap.id,
                    mapBounds: getMapBounds(),
                });
            };

            const ionicPage = newMap.element.closest('.ion-page');
            if (Capacitor.getPlatform() === 'ios' && ionicPage) {
                ionicPage.addEventListener('ionViewWillEnter', () => {
                    setTimeout(() => {
                        onDisplay();
                    }, 100);
                });
                ionicPage.addEventListener('ionViewDidEnter', () => {
                    setTimeout(() => {
                        onDisplay();
                    }, 100);
                });
            }

            const lastState = {
                width: elementBounds.width,
                height: elementBounds.height,
                isHidden: false,
            };
            newMap.resizeObserver = new ResizeObserver(() => {
                if (newMap.element != null) {
                    const mapRect = newMap.element.getBoundingClientRect();

                    const isHidden = mapRect.width === 0 && mapRect.height === 0;
                    if (!isHidden) {
                        if (lastState.isHidden) {
                            if (Capacitor.getPlatform() === 'ios' && !ionicPage) {
                                onDisplay();
                            }
                        } else if (
                            lastState.width !== mapRect.width ||
                            lastState.height !== mapRect.height
                        ) {
                            onResize();
                        }
                    }

                    lastState.width = mapRect.width;
                    lastState.height = mapRect.height;
                    lastState.isHidden = isHidden;
                }
            });
            newMap.resizeObserver.observe(newMap.element);
        }

        // small delay to allow for iOS WKWebView to setup corresponding element sub-scroll views ???
        await new Promise((resolve, reject) => {
            setTimeout(async () => {
                try {
                    await CapacitorAMap.create(options);
                    resolve(undefined);
                } catch (err) {
                    reject(err);
                }
            }, 200);
        });

        if (callback) {
            const onMapReadyListener = await CapacitorAMap.addListener(
                'onMapReady',
                (data: MapReadyCallbackData) => {
                    if (data.mapId == newMap.id) {
                        callback(data);
                        onMapReadyListener.remove();
                    }
                },
            );
        }

        return newMap;
    }

    private static async getElementBounds(element: HTMLElement): Promise<DOMRect> {
        return new Promise(resolve => {
            let elementBounds = element.getBoundingClientRect();
            if (elementBounds.width == 0) {
                let retries = 0;
                const boundsInterval = setInterval(function () {
                    if (elementBounds.width == 0 && retries < 30) {
                        elementBounds = element.getBoundingClientRect();
                        retries++;
                    } else {
                        if (retries == 30) {
                            console.warn('Map size could not be determined');
                        }
                        clearInterval(boundsInterval);
                        resolve(elementBounds);
                    }
                }, 100);
            } else {
                resolve(elementBounds);
            }
        });
    }

    public async destroy(): Promise<void> {
        if (Capacitor.getPlatform() == 'android') {
            this.disableScrolling();
        }

        if (Capacitor.isNativePlatform()) {
            this.resizeObserver?.disconnect();
        }

        this.removeAllMapListeners();

        return CapacitorAMap.destroy({
            id: this.id,
        });
    }

    public enableTouch(): Promise<void> {
        return CapacitorAMap.enableTouch({ id: this.id });
    }

    public disableTouch(): Promise<void> {
        return CapacitorAMap.disableTouch({ id: this.id });
    }

    private initScrolling(): void {
        const ionContents = document.getElementsByTagName('ion-content');

        // eslint-disable-next-line @typescript-eslint/prefer-for-of
        for (let i = 0; i < ionContents.length; i++) {
            (ionContents[i] as any).scrollEvents = true;
        }

        window.addEventListener('ionScroll', this.handleScrollEvent);
        window.addEventListener('scroll', this.handleScrollEvent);
        window.addEventListener('resize', this.handleScrollEvent);
        if (screen.orientation) {
            screen.orientation.addEventListener('change', () => {
                setTimeout(this.updateMapBounds, 500);
            });
        } else {
            window.addEventListener('orientationchange', () => {
                setTimeout(this.updateMapBounds, 500);
            });
        }
    }

    private disableScrolling(): void {
        window.removeEventListener('ionScroll', this.handleScrollEvent);
        window.removeEventListener('scroll', this.handleScrollEvent);
        window.removeEventListener('resize', this.handleScrollEvent);
        if (screen.orientation) {
            screen.orientation.removeEventListener('change', () => {
                setTimeout(this.updateMapBounds, 1000);
            });
        } else {
            window.removeEventListener('orientationchange', () => {
                setTimeout(this.updateMapBounds, 1000);
            });
        }
    }

    private handleScrollEvent = (): void => this.updateMapBounds();

    private updateMapBounds(): void {
        if (this.element) {
            const mapRect = this.element.getBoundingClientRect();

            CapacitorAMap.onScroll({
                id: this.id,
                mapBounds: {
                    x: mapRect.x,
                    y: mapRect.y,
                    width: mapRect.width,
                    height: mapRect.height,
                },
            });
        }
    }

    private async removeAllMapListeners(): Promise<void> {
        if (this.onBoundsChangedListener) {
            this.onBoundsChangedListener.remove();
            this.onBoundsChangedListener = undefined;
        }
        if (this.onCameraIdleListener) {
            this.onCameraIdleListener.remove();
            this.onCameraIdleListener = undefined;
        }
        if (this.onCameraMoveStartedListener) {
            this.onCameraMoveStartedListener.remove();
            this.onCameraMoveStartedListener = undefined;
        }

        if (this.onClusterClickListener) {
            this.onClusterClickListener.remove();
            this.onClusterClickListener = undefined;
        }

        if (this.onClusterInfoWindowClickListener) {
            this.onClusterInfoWindowClickListener.remove();
            this.onClusterInfoWindowClickListener = undefined;
        }

        if (this.onInfoWindowClickListener) {
            this.onInfoWindowClickListener.remove();
            this.onInfoWindowClickListener = undefined;
        }

        if (this.onMapClickListener) {
            this.onMapClickListener.remove();
            this.onMapClickListener = undefined;
        }

        if (this.onPolylineClickListener) {
            this.onPolylineClickListener.remove();
            this.onPolylineClickListener = undefined;
        }

        if (this.onMarkerClickListener) {
            this.onMarkerClickListener.remove();
            this.onMarkerClickListener = undefined;
        }

        if (this.onPolygonClickListener) {
            this.onPolygonClickListener.remove();
            this.onPolygonClickListener = undefined;
        }

        if (this.onCircleClickListener) {
            this.onCircleClickListener.remove();
            this.onCircleClickListener = undefined;
        }

        if (this.onMarkerDragStartListener) {
            this.onMarkerDragStartListener.remove();
            this.onMarkerDragStartListener = undefined;
        }

        if (this.onMarkerDragListener) {
            this.onMarkerDragListener.remove();
            this.onMarkerDragListener = undefined;
        }

        if (this.onMarkerDragEndListener) {
            this.onMarkerDragEndListener.remove();
            this.onMarkerDragEndListener = undefined;
        }

        if (this.onMyLocationButtonClickListener) {
            this.onMyLocationButtonClickListener.remove();
            this.onMyLocationButtonClickListener = undefined;
        }

        if (this.onMyLocationClickListener) {
            this.onMyLocationClickListener.remove();
            this.onMyLocationClickListener = undefined;
        }
    }
}
