import dayjs from 'dayjs/esm';
import { IQuartier } from 'app/entities/quartier/quartier.model';

export interface ICollecteurOdeur {
  id?: number;
  nomCollecteur?: string | null;
  prenomCollecteur?: string | null;
  date?: dayjs.Dayjs | null;
  tel1?: string | null;
  quartier?: IQuartier | null;
}

export class CollecteurOdeur implements ICollecteurOdeur {
  constructor(
    public id?: number,
    public nomCollecteur?: string | null,
    public prenomCollecteur?: string | null,
    public date?: dayjs.Dayjs | null,
    public tel1?: string | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getCollecteurOdeurIdentifier(collecteurOdeur: ICollecteurOdeur): number | undefined {
  return collecteurOdeur.id;
}
