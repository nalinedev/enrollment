import dayjs from 'dayjs';

import { IAgriculturalActivity } from 'app/shared/model/agricultural-activity.model';
import { IAquacultureActivity } from 'app/shared/model/aquaculture-activity.model';
import { IEconomicActivityType } from 'app/shared/model/economic-activity-type.model';
import { EconomicActivityStatus } from 'app/shared/model/enumerations/economic-activity-status.model';
import { ILivestockActivity } from 'app/shared/model/livestock-activity.model';
import { ILocation } from 'app/shared/model/location.model';
import { IMember } from 'app/shared/model/member.model';

export interface IEconomicActivity {
  id?: number;
  name?: string;
  description?: string | null;
  mainActivity?: boolean;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  annualRevenue?: number | null;
  monthlyRevenue?: number | null;
  numberOfEmployees?: number | null;
  status?: keyof typeof EconomicActivityStatus;
  notes?: string | null;
  agriculturalActivity?: IAgriculturalActivity | null;
  livestockActivity?: ILivestockActivity | null;
  aquacultureActivity?: IAquacultureActivity | null;
  member?: IMember | null;
  activityType?: IEconomicActivityType | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IEconomicActivity> = {
  mainActivity: false,
};
