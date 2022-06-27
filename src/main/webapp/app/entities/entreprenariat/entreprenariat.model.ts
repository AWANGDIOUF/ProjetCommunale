import { TypeEntreprenariat } from 'app/entities/enumerations/type-entreprenariat.model';

export interface IEntreprenariat {
  id?: number;
  typeEntre?: TypeEntreprenariat | null;
  autreEntre?: string | null;
}

export class Entreprenariat implements IEntreprenariat {
  constructor(public id?: number, public typeEntre?: TypeEntreprenariat | null, public autreEntre?: string | null) {}
}

export function getEntreprenariatIdentifier(entreprenariat: IEntreprenariat): number | undefined {
  return entreprenariat.id;
}
