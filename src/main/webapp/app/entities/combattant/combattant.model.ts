import * as dayjs from 'dayjs';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { IClub } from 'app/entities/club/club.model';

export interface ICombattant {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  dateNais?: dayjs.Dayjs | null;
  lieuNais?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  combattant?: IVainqueur | null;
  clubs?: IClub[] | null;
}

export class Combattant implements ICombattant {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public dateNais?: dayjs.Dayjs | null,
    public lieuNais?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public combattant?: IVainqueur | null,
    public clubs?: IClub[] | null
  ) {}
}

export function getCombattantIdentifier(combattant: ICombattant): number | undefined {
  return combattant.id;
}
