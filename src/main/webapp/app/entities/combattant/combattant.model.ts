import dayjs from 'dayjs/esm';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { IClub } from 'app/entities/club/club.model';

export interface ICombattant {
  id?: number;
  nomCombattant?: string | null;
  prenomCombattant?: string | null;
  dateNaisCombattant?: dayjs.Dayjs | null;
  lieuNaisCombattant?: string | null;
  photoCombattantContentType?: string | null;
  photoCombattant?: string | null;
  vainqueurs?: IVainqueur[] | null;
  club?: IClub | null;
}

export class Combattant implements ICombattant {
  constructor(
    public id?: number,
    public nomCombattant?: string | null,
    public prenomCombattant?: string | null,
    public dateNaisCombattant?: dayjs.Dayjs | null,
    public lieuNaisCombattant?: string | null,
    public photoCombattantContentType?: string | null,
    public photoCombattant?: string | null,
    public vainqueurs?: IVainqueur[] | null,
    public club?: IClub | null
  ) {}
}

export function getCombattantIdentifier(combattant: ICombattant): number | undefined {
  return combattant.id;
}
