import { WebPlugin } from '@capacitor/core';

import type {
  PhoneCallOptions,
  PhoneWellPlugin,
  SucessCallBack,
} from './definitions';

export class PhoneWellWeb extends WebPlugin implements PhoneWellPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async detectCallState(options: {
    action: string;
  }): Promise<{ action: string }> {
    console.log('Web is not supported.', options);
    return options;
  }

  async start(options: PhoneCallOptions): Promise<SucessCallBack> {
    console.log(options);
    return { msg: '成功' };
  }
}
