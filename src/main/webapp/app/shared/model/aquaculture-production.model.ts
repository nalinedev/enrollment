import dayjs from 'dayjs';

import { IAquacultureActivity } from 'app/shared/model/aquaculture-activity.model';
import { AquacultureProductionStatus } from 'app/shared/model/enumerations/aquaculture-production-status.model';

export interface IAquacultureProduction {
  id?: number;
  productionDate?: dayjs.Dayjs | null;
  numberOfAnimals?: number | null;
  stockingDensity?: number | null;
  productionQuantity?: number | null;
  productionUnit?: string | null;
  averageWeightGrams?: number | null;
  mortalityCount?: number | null;
  stockingCount?: number | null;
  harvestedCount?: number | null;
  expectedProduction?: number | null;
  expectedHarvestDate?: dayjs.Dayjs | null;
  actualHarvestDate?: dayjs.Dayjs | null;
  status?: keyof typeof AquacultureProductionStatus;
  notes?: string | null;
  aquacultureActivity?: IAquacultureActivity | null;
}

export const defaultValue: Readonly<IAquacultureProduction> = {};
