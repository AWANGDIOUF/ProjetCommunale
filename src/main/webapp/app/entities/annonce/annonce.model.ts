import dayjs from 'dayjs/esm';
import { IDon } from 'app/entities/don/don.model';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { IDonSang } from 'app/entities/don-sang/don-sang.model';
import { IVaccination } from 'app/entities/vaccination/vaccination.model';

export interface IAnnonce {
  id?: number;
  titre?: string | null;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  lieu?: string | null;
  don?: IDon | null;
  beneficiaire?: IBeneficiaire | null;
  donSangs?: IDonSang[] | null;
  vaccinations?: IVaccination[] | null;
}

export class Annonce implements IAnnonce {
  constructor(
    public id?: number,
    public titre?: string | null,
    public description?: string | null,
    public date?: dayjs.Dayjs | null,
    public lieu?: string | null,
    public don?: IDon | null,
    public beneficiaire?: IBeneficiaire | null,
    public donSangs?: IDonSang[] | null,
    public vaccinations?: IVaccination[] | null
  ) {}
}

export function getAnnonceIdentifier(annonce: IAnnonce): number | undefined {
  return annonce.id;
}
