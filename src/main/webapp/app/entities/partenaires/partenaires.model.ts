import { IEntrepreneur } from 'app/entities/entrepreneur/entrepreneur.model';
import { IEleveur } from 'app/entities/eleveur/eleveur.model';

export interface IPartenaires {
  id?: number;
  nomPartenaire?: string | null;
  emailPartenaire?: string | null;
  telPartenaire?: string | null;
  description?: string | null;
  entrepreneur?: IEntrepreneur | null;
  eleveur?: IEleveur | null;
}

export class Partenaires implements IPartenaires {
  constructor(
    public id?: number,
    public nomPartenaire?: string | null,
    public emailPartenaire?: string | null,
    public telPartenaire?: string | null,
    public description?: string | null,
    public entrepreneur?: IEntrepreneur | null,
    public eleveur?: IEleveur | null
  ) {}
}

export function getPartenairesIdentifier(partenaires: IPartenaires): number | undefined {
  return partenaires.id;
}
