import { registerPlugin } from '@capacitor/core';

import type { AmapPlugin } from './definitions';

const Amap = registerPlugin<AmapPlugin>('Amap');

export * from './definitions';
export { Amap };
