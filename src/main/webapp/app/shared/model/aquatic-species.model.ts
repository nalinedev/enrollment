export interface IAquaticSpecies {
  id?: number;
  code?: string;
  name?: string;
  scientificName?: string | null;
  category?: string | null;
  description?: string | null;
  freshwater?: boolean | null;
  saltwater?: boolean | null;
  active?: boolean;
}

export const defaultValue: Readonly<IAquaticSpecies> = {
  freshwater: false,
  saltwater: false,
  active: false,
};
