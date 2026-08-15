import { LocationType } from 'app/shared/model/enumerations/location-type.model';

export interface ILocation {
  id?: number;
  code?: string;
  name?: string;
  type?: keyof typeof LocationType;
  addressLine1?: string | null;
  addressLine2?: string | null;
  postalCode?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  description?: string | null;
  parent?: ILocation | null;
}

export const defaultValue: Readonly<ILocation> = {};
