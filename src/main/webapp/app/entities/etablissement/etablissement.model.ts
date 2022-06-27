import dayjs from 'dayjs/esm';
import { IEnsegnant } from 'app/entities/ensegnant/ensegnant.model';
import { IResultatExamen } from 'app/entities/resultat-examen/resultat-examen.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';

export interface IEtablissement {
  id?: number;
  nomEtat?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  description?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
  ensegnants?: IEnsegnant[] | null;
  resultatExamen?: IResultatExamen[] | null;
  quartier?: IQuartier | null;
}

export class Etablissement implements IEtablissement {
  constructor(
    public id?: number,
    public nomEtat?: string | null,
    public dateCreation?: dayjs.Dayjs | null,
    public description?: string | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public ensegnants?: IEnsegnant[] | null,
    public resultatExamen?: IResultatExamen[] | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getEtablissementIdentifier(etablissement: IEtablissement): number | undefined {
  return etablissement.id;
}
