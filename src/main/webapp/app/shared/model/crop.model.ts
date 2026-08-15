export interface ICrop {
  id?: number;
  code?: string;
  name?: string;
  scientificName?: string | null;
  category?: string | null;
  description?: string | null;
  perennial?: boolean | null;
  active?: boolean;
}

export const defaultValue: Readonly<ICrop> = {
  perennial: false,
  active: false,
};
