import dayjs from 'dayjs';

import { IAgriculturalActivity } from 'app/shared/model/agricultural-activity.model';
import { ICropVariety } from 'app/shared/model/crop-variety.model';
import { ICrop } from 'app/shared/model/crop.model';
import { ProductionStatus } from 'app/shared/model/enumerations/production-status.model';

export interface IAgriculturalProduction {
  id?: number;
  area?: number;
  areaUnit?: string;
  plantingDate?: dayjs.Dayjs | null;
  harvestStartDate?: dayjs.Dayjs | null;
  harvestEndDate?: dayjs.Dayjs | null;
  productionQuantity?: number | null;
  productionUnit?: string | null;
  expectedAnnualProduction?: number | null;
  numberOfPlants?: number | null;
  plantingDensity?: number | null;
  productionYear?: number | null;
  status?: keyof typeof ProductionStatus;
  notes?: string | null;
  agriculturalActivity?: IAgriculturalActivity | null;
  crop?: ICrop | null;
  cropVariety?: ICropVariety | null;
}

export const defaultValue: Readonly<IAgriculturalProduction> = {};
