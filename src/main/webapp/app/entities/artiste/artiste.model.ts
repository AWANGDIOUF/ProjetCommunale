import { Domaine } from 'app/entities/enumerations/domaine.model';

export interface IArtiste {
  id?: number;
  nomArtiste?: string | null;
  prenomArtiste?: string | null;
  domaine?: Domaine | null;
  autreDomaine?: string | null;
}

export class Artiste implements IArtiste {
  constructor(
    public id?: number,
    public nomArtiste?: string | null,
    public prenomArtiste?: string | null,
    public domaine?: Domaine | null,
    public autreDomaine?: string | null
  ) {}
}

export function getArtisteIdentifier(artiste: IArtiste): number | undefined {
  return artiste.id;
}
