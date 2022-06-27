import { IArtiste } from 'app/entities/artiste/artiste.model';

export interface IInterviewsArtiste {
  id?: number;
  titre?: string | null;
  description?: string | null;
  lien?: string | null;
  artiste?: IArtiste | null;
}

export class InterviewsArtiste implements IInterviewsArtiste {
  constructor(
    public id?: number,
    public titre?: string | null,
    public description?: string | null,
    public lien?: string | null,
    public artiste?: IArtiste | null
  ) {}
}

export function getInterviewsArtisteIdentifier(interviewsArtiste: IInterviewsArtiste): number | undefined {
  return interviewsArtiste.id;
}
