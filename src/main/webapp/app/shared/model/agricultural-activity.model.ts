import dayjs from 'dayjs';

import { AgriculturalExploitationMode } from 'app/shared/model/enumerations/agricultural-exploitation-mode.model';
import { LandOwnershipType } from 'app/shared/model/enumerations/land-ownership-type.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IAgriculturalActivity {
  id?: number;
  totalArea?: number;
  areaUnit?: string;
  exploitationMode?: keyof typeof AgriculturalExploitationMode | null;
  ownershipType?: keyof typeof LandOwnershipType | null;
  startDate?: dayjs.Dayjs | null;
  irrigationAvailable?: boolean | null;
  organicProduction?: boolean | null;
  certification?: string | null;
  description?: string | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IAgriculturalActivity> = {
  irrigationAvailable: false,
  organicProduction: false,
};
