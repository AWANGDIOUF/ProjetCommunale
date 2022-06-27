import dayjs from 'dayjs/esm';
import { IQuartier } from 'app/entities/quartier/quartier.model';

export interface IRecuperationRecyclable {
  id?: number;
  nom?: string | null;
  date?: dayjs.Dayjs | null;
  lieu?: string | null;
  quartier?: IQuartier | null;
}

export class RecuperationRecyclable implements IRecuperationRecyclable {
  constructor(
    public id?: number,
    public nom?: string | null,
    public date?: dayjs.Dayjs | null,
    public lieu?: string | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getRecuperationRecyclableIdentifier(recuperationRecyclable: IRecuperationRecyclable): number | undefined {
  return recuperationRecyclable.id;
}
