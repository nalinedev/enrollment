export interface IEconomicActivityType {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  sector?: string | null;
  active?: boolean;
}

export const defaultValue: Readonly<IEconomicActivityType> = {
  active: false,
};
