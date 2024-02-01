import { WebPlugin } from '@capacitor/core';

import type { AmapPlugin } from './definitions';

export class AmapWeb extends WebPlugin implements AmapPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
