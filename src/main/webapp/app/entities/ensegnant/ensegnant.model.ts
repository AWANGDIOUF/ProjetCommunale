import { IProposition } from 'app/entities/proposition/proposition.model';
import { ILienTutoriel } from 'app/entities/lien-tutoriel/lien-tutoriel.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';

export interface IEnsegnant {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  email?: string;
  tel?: string;
  tel1?: string | null;
  propositions?: IProposition[] | null;
  lienTutoriels?: ILienTutoriel[] | null;
  etablissement?: IEtablissement | null;
}

export class Ensegnant implements IEnsegnant {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public email?: string,
    public tel?: string,
    public tel1?: string | null,
    public propositions?: IProposition[] | null,
    public lienTutoriels?: ILienTutoriel[] | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getEnsegnantIdentifier(ensegnant: IEnsegnant): number | undefined {
  return ensegnant.id;
}
