import { Capacitor } from '@capacitor/core';
import type { PluginListenerHandle } from '@capacitor/core';

import { CameraPosition, GetFromLocationArgs, MapListenerCallback, MapReadyCallbackData, MapStatusLimits, MapType, UiSettings } from './definitions';
import type { CreateMapArgs, MyLocationStyle } from './implementation';
import { CapacitorAMap } from './implementation';

/**
 * 高德地图 SDK 的 JavaScript 接口。
 * @class AMap
 */
export interface AMapInterface {
    /**
     * 逆地理编码（坐标转地址）
     * @function AMap.getFromLocation
     * @since 0.0.8
     */
    getFromLocation(args: GetFromLocationArgs): Promise<{
        code: number;
        address: any;
    }>;

    /**
     * 更新隐私合规状态，需要在初始化地图之前完成。
     * @function AMap.updatePrivacyShow
     * @param isContains 隐私权政策是否包含高德开平隐私权政策  true是包含 
     * @param isShow 隐私权政策是否弹窗展示告知用户 true是展示 
     * @since 0.0.2
     */
    updatePrivacyShow(isContains: boolean, isShow: boolean): Promise<void>;
    /**
     * 更新同意隐私状态，需要在初始化地图之前完成。
     * @function AMap.updatePrivacyAgree
     * @param isAgree 隐私权政策是否取得用户同意  true是用户同意
     * @since 0.0.2
     */
    updatePrivacyAgree(isAgree: boolean): Promise<void>;
    /**
     * 是否打开地形图, 打开地形图之后，底图会变成3D模式，添加的点线面等覆盖物也会自动带有高程。注意：需要在MapView创建之前调用。
     * @function AMap.setTerrainEnable
     * @param isTerrainEnable true为打开，默认false
     * @default false
     * @since 0.0.5
     */
    setTerrainEnable(isTerrainEnable: boolean): Promise<void>;
    /**
     * 启动离线地图组件。
     * @function AMap.openOfflineMapActivity
     * @since 0.0.7
     */
    openOfflineMapActivity(): Promise<void>;

    /**
     * 创建地图实例。
     * @function AMap.create
     * @param {CreateMapArgs} options - 创建地图的参数。
     * @param callback
     * @since 0.0.1
     */
    create(options: CreateMapArgs, callback?: MapListenerCallback<MapReadyCallbackData>): Promise<AMap>;

    /**
     * 设置是否显示室内地图，默认不显示。注：如果打开了室内地图，会显示3D建筑物，即如果之前有设置不显示3D建筑物，3D建筑物也会被显示出来。
     * @param enable true：显示室内地图；false：不显示
     * @since 0.0.5
     */
    showIndoorMap(enable: boolean): Promise<void>;
    /**
     * 设置地图模式。
     * @since 0.0.5
     */
    setMapType(type: MapType): Promise<void>;
    /**
     * 设置是否打开交通路况图层。
     * @param enable 是否打开交通路况图层。
     * @since 0.0.5
     */
    setTrafficEnabled(enable: boolean): Promise<void>;

    /**
     * 销毁地图实例。
     * @since 0.0.1
     */
    destroy(): Promise<void>;
    /**
     * 显示地图。
     * @since 0.0.7
     */
    show(): Promise<void>;
    /**
     * 隐藏地图。
     * @since 0.0.7
     */
    hide(): Promise<void>;
    /**
     * 设置地图允许被触控。
     * @since 0.0.1
     */
    enableTouch(): Promise<void>;
    /**
     * 设置地图禁止被触控。
     * @since 0.0.1
     */
    disableTouch(): Promise<void>;

    /**
     * 设置启动显示定位蓝点。
     * @since 0.0.5
     */
    enableMyLocation(): Promise<void>;
    /**
     * 设置隐藏定位蓝点并不进行定位。
     * @since 0.0.5
     */
    disableMyLocation(): Promise<void>;
    /**
     * 设置定位蓝点的Style。
     * @since 0.0.5
     */
    setMyLocationStyle(style: MyLocationStyle): Promise<void>;

    /**
     * 设置地图内置UI及手势控制器。
     * @since 0.0.5
     */
    setUiSettings(args: UiSettings): Promise<void>;


    /**
     * 给地图设置一个新的状态。
     * @param args 新的地图状态。
     * @since 0.0.6
     */
    cameraUpdatePosition(args: CameraPosition): Promise<void>;
    /**
     * 设置地图缩放级别。
     * @param zoom 地图缩放级别。地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。
     * @since 0.0.6
     */
    cameraZoomTo(zoom: Number): Promise<void>;
    /**
     * 设置地图显示范围，无论如何操作地图，显示区域都不能超过该矩形区域。
     * @since 0.0.6
     */
    setMapStatusLimits(args: MapStatusLimits): Promise<void>;

    /**
     * 设置地图状态的监听接口。
     * @since 0.0.2
     */
    setOnCameraChangeListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置室内地图状态监听接口。
     * @since 0.0.2
     */
    setOnIndoorBuildingActiveListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置marker的信息窗口点击事件监听接口。
     * @since 0.0.2
     */
    setOnInfoWindowClickListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置地图点击事件监听接口。
     * @since 0.0.2
     */
    setOnMapClickListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置地图加载完成监听接口。
     * @since 0.0.2
     */
    setOnMapLoadedListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置地图长按事件监听接口。
     * @since 0.0.2
     */
    setOnMapLongClickListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置地图触摸事件监听接口。
     * @since 0.0.2
     */
    setOnMapTouchListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置marker点击事件监听接口。
     * @since 0.0.2
     */
    setOnMarkerClickListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * marker拖动事件监听接口。
     * @since 0.0.2
     */
    setOnMarkerDragListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置海量点单击事件监听。
     * @since 0..2
     */
    setOnMultiPointClickListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置用户定位信息监听接口。
     * @since 0.0.2
     */
    setOnMyLocationChangeListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置底图poi点击事件监听接口。
     * @since 0.0.2
     */
    setOnPOIClickListener(callback?: MapListenerCallback<any>): Promise<void>;
    /**
     * 设置polyline点击事件监听接口。
     * @since 0.0.2
     */
    setOnPolylineClickListener(callback?: MapListenerCallback<any>): Promise<void>;
}

export class AMap implements AMapInterface {
    /**
     * 地图实例的唯一标识符。
     */
    private id: string;
    private element: HTMLElement | null = null;
    private resizeObserver: ResizeObserver | null = null;

    private onCameraChangeListener?: PluginListenerHandle;
    private onCameraChangeFinishListener?: PluginListenerHandle;
    private onIndoorBuildingListener?: PluginListenerHandle;
    private onInfoWindowClickListener?: PluginListenerHandle;
    private onMapClickListener?: PluginListenerHandle;
    private onMapReadyListener?: PluginListenerHandle;
    private onMapLongClickListener?: PluginListenerHandle;
    private onMapTouchListener?: PluginListenerHandle;
    private onMarkerClickListener?: PluginListenerHandle;
    private onMarkerDragStartListener?: PluginListenerHandle;
    private onMarkerDragListener?: PluginListenerHandle;
    private onMarkerDragEndListener?: PluginListenerHandle;
    private onMultiPointClickListener?: PluginListenerHandle;
    private onMyLocationChangeListener?: PluginListenerHandle;
    private onPOIClickListener?: PluginListenerHandle;
    private onPolylineClickListener?: PluginListenerHandle;

    private constructor(id: string) {
        this.id = id;
    }

    public static getFromLocation(args: GetFromLocationArgs): Promise<{ code: number; address: any; }> {
        return CapacitorAMap.getFromLocation(args);
    }

    public static updatePrivacyShow(isContains: boolean, isShow: boolean): Promise<void> {
        return CapacitorAMap.updatePrivacyShow({ isContains, isShow });
    }

    public static updatePrivacyAgree(isAgree: boolean): Promise<void> {
        return CapacitorAMap.updatePrivacyAgree({ isAgree });
    }

    public static setTerrainEnable(isTerrainEnable: boolean): Promise<void> {
        return CapacitorAMap.setTerrainEnable({ isTerrainEnable });
    }

    public static openOfflineMapActivity(): Promise<void> {
        return CapacitorAMap.openOfflineMapActivity();
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

            const getMapBounds = () => newMap.element?.getBoundingClientRect() ?? ({} as DOMRect);

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

    public showIndoorMap(enable: boolean): Promise<void> {
        return CapacitorAMap.showIndoorMap({ id: this.id, enable });
    }

    public setMapType(type: MapType): Promise<void> {
        return CapacitorAMap.setMapType({ id: this.id, type });
    }

    public setTrafficEnabled(enable: boolean): Promise<void> {
        return CapacitorAMap.setTrafficEnabled({ id: this.id, enable });
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

    /**
     * @deprecated Use AMap.getFromLocation instead.
     */
    public getFromLocation(_args: GetFromLocationArgs): Promise<{ code: number; address: any; }> {
        throw new Error('Method not implemented.');
    }

    /**
     * @deprecated Use AMap.updatePrivacyShow instead.
     */
    public updatePrivacyShow(_isContains: boolean, _isShow: boolean): Promise<void> {
        throw new Error('Method not implemented.');
    }

    /**
     * @deprecated Use AMap.updatePrivacyAgree instead.
     */
    public updatePrivacyAgree(_isAgree: boolean): Promise<void> {
        throw new Error('Method not implemented.');
    }

    /**
     * @deprecated Use AMap.setTerrainEnable instead.
     */
    public setTerrainEnable(_isTerrainEnable: boolean): Promise<void> {
        throw new Error('Method not implemented.');
    }

    /**
     * @deprecated Use AMap.openOfflineMapActivity instead.
     */
    public openOfflineMapActivity(): Promise<void> {
        throw new Error('Method not implemented.');
    }

    /**
     * @deprecated Use AMap.create instead.
     */
    public create(_options: CreateMapArgs, _callback?: MapListenerCallback<MapReadyCallbackData> | undefined): Promise<AMap> {
        throw new Error('Method not implemented.');
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

    public show(): Promise<void> {
        return CapacitorAMap.show({ id: this.id });
    }

    public hide(): Promise<void> {
        return CapacitorAMap.hide({ id: this.id });
    }

    public enableTouch(): Promise<void> {
        return CapacitorAMap.enableTouch({ id: this.id });
    }

    public disableTouch(): Promise<void> {
        return CapacitorAMap.disableTouch({ id: this.id });
    }

    public enableMyLocation(): Promise<void> {
        return CapacitorAMap.enableMyLocation({ id: this.id });
    }

    public disableMyLocation(): Promise<void> {
        return CapacitorAMap.disableMyLocation({ id: this.id });
    }

    public setMyLocationStyle(style: MyLocationStyle): Promise<void> {
        return CapacitorAMap.setMyLocationStyle({
            id: this.id,
            style,
        });
    }

    public setUiSettings(args: UiSettings): Promise<void> {
        return CapacitorAMap.setUiSettings({ id: this.id, ...args });
    }

    public cameraUpdatePosition(args: CameraPosition): Promise<void> {
        return CapacitorAMap.cameraUpdatePosition({ id: this.id, cameraOptions: args });
    }

    public cameraZoomTo(zoom: Number): Promise<void> {
        return CapacitorAMap.cameraZoomTo({ id: this.id, zoom });
    }

    public setMapStatusLimits(args: MapStatusLimits): Promise<void> {
        return CapacitorAMap.setMapStatusLimits({ id: this.id, ...args });
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
                mapBounds: mapRect,
            });
        }
    }

    public async setOnCameraChangeListener(callback?: MapListenerCallback<any>): Promise<void> {
        if (this.onCameraChangeListener) {
            this.onCameraChangeListener.remove();
        }

        if (callback) {
            this.onCameraChangeListener = await CapacitorAMap.addListener('onCameraChange', this.generateCallback(callback));
        } else {
            this.onCameraChangeListener = undefined;
        }
    }

    public async setOnIndoorBuildingActiveListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onIndoorBuildingListener) {
            this.onIndoorBuildingListener.remove();
        }

        if (callback) {
            this.onIndoorBuildingListener = await CapacitorAMap.addListener('onIndoorBuildingActive', this.generateCallback(callback));
        } else {
            this.onIndoorBuildingListener = undefined;
        }
    }

    public async setOnInfoWindowClickListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onInfoWindowClickListener) {
            this.onInfoWindowClickListener.remove();
        }

        if (callback) {
            this.onInfoWindowClickListener = await CapacitorAMap.addListener('onInfoWindowClick', this.generateCallback(callback));
        } else {
            this.onInfoWindowClickListener = undefined;
        }
    }

    public async setOnMapClickListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMapClickListener) {
            this.onMapClickListener.remove();
        }

        if (callback) {
            this.onMapClickListener = await CapacitorAMap.addListener('onMapClick', this.generateCallback(callback));
        } else {
            this.onMapClickListener = undefined;
        }
    }

    public async setOnMapLoadedListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMapReadyListener) {
            this.onMapReadyListener.remove();
        }

        if (callback) {
            this.onMapReadyListener = await CapacitorAMap.addListener('onMapReady', this.generateCallback(callback));
        } else {
            this.onMapReadyListener = undefined;
        }
    }

    public async setOnMapLongClickListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMapLongClickListener) {
            this.onMapLongClickListener.remove();
        }

        if (callback) {
            this.onMapLongClickListener = await CapacitorAMap.addListener('onMapLongClick', this.generateCallback(callback));
        } else {
            this.onMapLongClickListener = undefined;
        }
    }

    public async setOnMapTouchListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMapTouchListener) {
            this.onMapTouchListener.remove();
        }

        if (callback) {
            this.onMapTouchListener = await CapacitorAMap.addListener('onMapTouch', this.generateCallback(callback));
        } else {
            this.onMapTouchListener = undefined;
        }
    }

    public async setOnMarkerClickListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMarkerClickListener) {
            this.onMarkerClickListener.remove();
        }

        if (callback) {
            this.onMarkerClickListener = await CapacitorAMap.addListener('onMarkerClick', this.generateCallback(callback));
        } else {
            this.onMarkerClickListener = undefined;
        }
    }

    public async setOnMarkerDragListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMarkerDragListener) {
            this.onMarkerDragListener.remove();
        }

        if (callback) {
            this.onMarkerDragListener = await CapacitorAMap.addListener('onMarkerDrag', this.generateCallback(callback));
        } else {
            this.onMarkerDragListener = undefined;
        }
    }

    public async setOnMultiPointClickListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMultiPointClickListener) {
            this.onMultiPointClickListener.remove();
        }

        if (callback) {
            this.onMultiPointClickListener = await CapacitorAMap.addListener('onMultiPointClick', this.generateCallback(callback));
        } else {
            this.onMultiPointClickListener = undefined;
        }
    }

    public async setOnMyLocationChangeListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onMyLocationChangeListener) {
            this.onMyLocationChangeListener.remove();
        }

        if (callback) {
            this.onMyLocationChangeListener = await CapacitorAMap.addListener('onMyLocationChange', this.generateCallback(callback));
        } else {
            this.onMyLocationChangeListener = undefined;
        }
    }

    public async setOnPOIClickListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onPOIClickListener) {
            this.onPOIClickListener.remove();
        }

        if (callback) {
            this.onPOIClickListener = await CapacitorAMap.addListener('onPOIClick', this.generateCallback(callback));
        } else {
            this.onPOIClickListener = undefined;
        }
    }

    public async setOnPolylineClickListener(callback?: MapListenerCallback<any> | undefined): Promise<void> {
        if (this.onPolylineClickListener) {
            this.onPolylineClickListener.remove();
        }

        if (callback) {
            this.onPolylineClickListener = await CapacitorAMap.addListener('onPolylineClick', this.generateCallback(callback));
        } else {
            this.onPolylineClickListener = undefined;
        }
    }

    private async removeAllMapListeners(): Promise<void> {
        if (this.onCameraChangeListener) {
            this.onCameraChangeListener.remove();
            this.onCameraChangeListener = undefined;
        }
        if (this.onCameraChangeFinishListener) {
            this.onCameraChangeFinishListener.remove();
            this.onCameraChangeFinishListener = undefined;
        }
        if (this.onIndoorBuildingListener) {
            this.onIndoorBuildingListener.remove();
            this.onIndoorBuildingListener = undefined;
        }

        if (this.onInfoWindowClickListener) {
            this.onInfoWindowClickListener.remove();
            this.onInfoWindowClickListener = undefined;
        }

        if (this.onMapClickListener) {
            this.onMapClickListener.remove();
            this.onMapClickListener = undefined;
        }

        if (this.onMapReadyListener) {
            this.onMapReadyListener.remove();
            this.onMapReadyListener = undefined;
        }

        if (this.onMapLongClickListener) {
            this.onMapLongClickListener.remove();
            this.onMapLongClickListener = undefined;
        }

        if (this.onMapTouchListener) {
            this.onMapTouchListener.remove();
            this.onMapTouchListener = undefined;
        }

        if (this.onMarkerClickListener) {
            this.onMarkerClickListener.remove();
            this.onMarkerClickListener = undefined;
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

        if (this.onMultiPointClickListener) {
            this.onMultiPointClickListener.remove();
            this.onMultiPointClickListener = undefined;
        }

        if (this.onMyLocationChangeListener) {
            this.onMyLocationChangeListener.remove();
            this.onMyLocationChangeListener = undefined;
        }

        if (this.onPOIClickListener) {
            this.onPOIClickListener.remove();
            this.onPOIClickListener = undefined;
        }

        if (this.onPolylineClickListener) {
            this.onPolylineClickListener.remove();
            this.onPolylineClickListener = undefined;
        }
    }

    private generateCallback(callback: MapListenerCallback<any>): MapListenerCallback<any> {
        const mapId = this.id;
        return (data: any) => {
            if (data.mapId == mapId) {
                callback(data);
            }
        };
    }
}