import { ICooperative } from 'app/shared/model/cooperative.model';
import { SequenceType } from 'app/shared/model/enumerations/sequence-type.model';

export interface INumberSequence {
  id?: number;
  sequenceType?: keyof typeof SequenceType;
  prefix?: string | null;
  year?: number | null;
  currentValue?: number;
  padding?: number;
  cooperative?: ICooperative | null;
}

export const defaultValue: Readonly<INumberSequence> = {};
