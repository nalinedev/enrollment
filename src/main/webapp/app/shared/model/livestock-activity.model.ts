import dayjs from 'dayjs';

import { LivestockOwnershipType } from 'app/shared/model/enumerations/livestock-ownership-type.model';
import { LivestockProductionMode } from 'app/shared/model/enumerations/livestock-production-mode.model';
import { LivestockProductionType } from 'app/shared/model/enumerations/livestock-production-type.model';
import { LivestockStatus } from 'app/shared/model/enumerations/livestock-status.model';
import { ILivestockType } from 'app/shared/model/livestock-type.model';
import { ILocation } from 'app/shared/model/location.model';

export interface ILivestockActivity {
  id?: number;
  name?: string;
  description?: string | null;
  productionMode?: keyof typeof LivestockProductionMode | null;
  ownershipType?: keyof typeof LivestockOwnershipType | null;
  productionType?: keyof typeof LivestockProductionType | null;
  startDate?: dayjs.Dayjs | null;
  totalArea?: number | null;
  areaUnit?: string | null;
  status?: keyof typeof LivestockStatus;
  numberOfAnimals?: number | null;
  annualRevenue?: number | null;
  monthlyRevenue?: number | null;
  employees?: number | null;
  veterinaryServiceAvailable?: boolean | null;
  feedSource?: string | null;
  waterSource?: string | null;
  certification?: string | null;
  notes?: string | null;
  location?: ILocation | null;
  livestockType?: ILivestockType | null;
}

export const defaultValue: Readonly<ILivestockActivity> = {
  veterinaryServiceAvailable: false,
};
