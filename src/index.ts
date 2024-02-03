/* eslint-disable @typescript-eslint/no-namespace */
export {

} from './definitions';
import { AMap } from './map';

export {
    AMap
};

declare global {
    export namespace JSX {
        export interface IntrinsicElements {
            'capacitor-amap': any;
        }
    }
}
