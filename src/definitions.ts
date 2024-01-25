export interface PhoneWellPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
