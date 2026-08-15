import dayjs from 'dayjs';

import { IAquaticSpecies } from 'app/shared/model/aquatic-species.model';
import { AquacultureOwnershipType } from 'app/shared/model/enumerations/aquaculture-ownership-type.model';
import { AquacultureProductionMode } from 'app/shared/model/enumerations/aquaculture-production-mode.model';
import { AquacultureProductionType } from 'app/shared/model/enumerations/aquaculture-production-type.model';
import { AquacultureStatus } from 'app/shared/model/enumerations/aquaculture-status.model';
import { AquacultureSystemType } from 'app/shared/model/enumerations/aquaculture-system-type.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IAquacultureActivity {
  id?: number;
  name?: string;
  description?: string | null;
  productionMode?: keyof typeof AquacultureProductionMode | null;
  ownershipType?: keyof typeof AquacultureOwnershipType | null;
  productionType?: keyof typeof AquacultureProductionType | null;
  systemType?: keyof typeof AquacultureSystemType | null;
  startDate?: dayjs.Dayjs | null;
  totalArea?: number | null;
  areaUnit?: string | null;
  waterSource?: string | null;
  numberOfProductionUnits?: number | null;
  productionUnitDescription?: string | null;
  status?: keyof typeof AquacultureStatus;
  annualRevenue?: number | null;
  monthlyRevenue?: number | null;
  employees?: number | null;
  certification?: string | null;
  notes?: string | null;
  location?: ILocation | null;
  aquaticSpecies?: IAquaticSpecies | null;
}

export const defaultValue: Readonly<IAquacultureActivity> = {};
