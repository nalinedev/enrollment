export interface ILivestockType {
  id?: number;
  code?: string;
  name?: string;
  scientificName?: string | null;
  category?: string | null;
  description?: string | null;
  active?: boolean;
}

export const defaultValue: Readonly<ILivestockType> = {
  active: false,
};
