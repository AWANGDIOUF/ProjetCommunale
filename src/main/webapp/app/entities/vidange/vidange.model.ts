import { IQuartier } from 'app/entities/quartier/quartier.model';

export interface IVidange {
  id?: number;
  nomVideur?: string | null;
  prenomVideur?: string | null;
  tel1?: string | null;
  tel2?: string | null;
  quartier?: IQuartier | null;
}

export class Vidange implements IVidange {
  constructor(
    public id?: number,
    public nomVideur?: string | null,
    public prenomVideur?: string | null,
    public tel1?: string | null,
    public tel2?: string | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getVidangeIdentifier(vidange: IVidange): number | undefined {
  return vidange.id;
}
