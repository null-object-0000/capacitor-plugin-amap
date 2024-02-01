import { registerPlugin } from '@capacitor/core';

import type { AmapPlugin } from './definitions';

const Amap = registerPlugin<AmapPlugin>('Amap', {
  web: () => import('./web').then(m => new m.AmapWeb()),
});

export * from './definitions';
export { Amap };
