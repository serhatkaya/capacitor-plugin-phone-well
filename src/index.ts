import { registerPlugin } from '@capacitor/core';

import type { PhoneWellPlugin } from './definitions';

const PhoneWell = registerPlugin<PhoneWellPlugin>('PhoneWell', {
  web: () => import('./web').then(m => new m.PhoneWellWeb()),
});

export * from './definitions';
export { PhoneWell };
