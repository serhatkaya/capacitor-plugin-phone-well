import { WebPlugin } from '@capacitor/core';

import type { PhoneWellPlugin } from './definitions';

export class PhoneWellWeb extends WebPlugin implements PhoneWellPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
