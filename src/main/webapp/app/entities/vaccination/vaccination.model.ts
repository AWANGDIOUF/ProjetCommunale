import dayjs from 'dayjs/esm';
import { ITypeVaccin } from 'app/entities/type-vaccin/type-vaccin.model';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { ICible } from 'app/entities/cible/cible.model';

export interface IVaccination {
  id?: number;
  date?: dayjs.Dayjs | null;
  description?: string | null;
  duree?: boolean | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  typeVaccin?: ITypeVaccin | null;
  annonce?: IAnnonce | null;
  cibles?: ICible[] | null;
}

export class Vaccination implements IVaccination {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public description?: string | null,
    public duree?: boolean | null,
    public dateDebut?: dayjs.Dayjs | null,
    public dateFin?: dayjs.Dayjs | null,
    public typeVaccin?: ITypeVaccin | null,
    public annonce?: IAnnonce | null,
    public cibles?: ICible[] | null
  ) {
    this.duree = this.duree ?? false;
  }
}

export function getVaccinationIdentifier(vaccination: IVaccination): number | undefined {
  return vaccination.id;
}
