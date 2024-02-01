export interface AmapPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
