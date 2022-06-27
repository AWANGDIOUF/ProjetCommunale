import { IEntreprenariat } from 'app/entities/entreprenariat/entreprenariat.model';
import { IDomaineActivite } from 'app/entities/domaine-activite/domaine-activite.model';

export interface IEntrepreneur {
  id?: number;
  nomEntrepreneur?: string;
  prenomEntrepreneur?: string | null;
  emailEntrepreneur?: string | null;
  telEntrepreneur?: string;
  tel1Entrepreneur?: string | null;
  entreprenariat?: IEntreprenariat | null;
  domaineActivite?: IDomaineActivite | null;
}

export class Entrepreneur implements IEntrepreneur {
  constructor(
    public id?: number,
    public nomEntrepreneur?: string,
    public prenomEntrepreneur?: string | null,
    public emailEntrepreneur?: string | null,
    public telEntrepreneur?: string,
    public tel1Entrepreneur?: string | null,
    public entreprenariat?: IEntreprenariat | null,
    public domaineActivite?: IDomaineActivite | null
  ) {}
}

export function getEntrepreneurIdentifier(entrepreneur: IEntrepreneur): number | undefined {
  return entrepreneur.id;
}
