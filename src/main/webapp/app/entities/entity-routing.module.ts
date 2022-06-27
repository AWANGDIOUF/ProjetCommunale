import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'equipe',
        data: { pageTitle: 'projetCommunaleApp.equipe.home.title' },
        loadChildren: () => import('./equipe/equipe.module').then(m => m.EquipeModule),
      },
      {
        path: 'type-sport',
        data: { pageTitle: 'projetCommunaleApp.typeSport.home.title' },
        loadChildren: () => import('./type-sport/type-sport.module').then(m => m.TypeSportModule),
      },
      {
        path: 'club',
        data: { pageTitle: 'projetCommunaleApp.club.home.title' },
        loadChildren: () => import('./club/club.module').then(m => m.ClubModule),
      },
      {
        path: 'joueur',
        data: { pageTitle: 'projetCommunaleApp.joueur.home.title' },
        loadChildren: () => import('./joueur/joueur.module').then(m => m.JoueurModule),
      },
      {
        path: 'combattant',
        data: { pageTitle: 'projetCommunaleApp.combattant.home.title' },
        loadChildren: () => import('./combattant/combattant.module').then(m => m.CombattantModule),
      },
      {
        path: 'quartier',
        data: { pageTitle: 'projetCommunaleApp.quartier.home.title' },
        loadChildren: () => import('./quartier/quartier.module').then(m => m.QuartierModule),
      },
      {
        path: 'archive-sport',
        data: { pageTitle: 'projetCommunaleApp.archiveSport.home.title' },
        loadChildren: () => import('./archive-sport/archive-sport.module').then(m => m.ArchiveSportModule),
      },
      {
        path: 'match',
        data: { pageTitle: 'projetCommunaleApp.match.home.title' },
        loadChildren: () => import('./match/match.module').then(m => m.MatchModule),
      },
      {
        path: 'competition',
        data: { pageTitle: 'projetCommunaleApp.competition.home.title' },
        loadChildren: () => import('./competition/competition.module').then(m => m.CompetitionModule),
      },
      {
        path: 'vainqueur',
        data: { pageTitle: 'projetCommunaleApp.vainqueur.home.title' },
        loadChildren: () => import('./vainqueur/vainqueur.module').then(m => m.VainqueurModule),
      },
      {
        path: 'don',
        data: { pageTitle: 'projetCommunaleApp.don.home.title' },
        loadChildren: () => import('./don/don.module').then(m => m.DonModule),
      },
      {
        path: 'donneur',
        data: { pageTitle: 'projetCommunaleApp.donneur.home.title' },
        loadChildren: () => import('./donneur/donneur.module').then(m => m.DonneurModule),
      },
      {
        path: 'beneficiaire',
        data: { pageTitle: 'projetCommunaleApp.beneficiaire.home.title' },
        loadChildren: () => import('./beneficiaire/beneficiaire.module').then(m => m.BeneficiaireModule),
      },
      {
        path: 'annonce',
        data: { pageTitle: 'projetCommunaleApp.annonce.home.title' },
        loadChildren: () => import('./annonce/annonce.module').then(m => m.AnnonceModule),
      },
      {
        path: 'vaccination',
        data: { pageTitle: 'projetCommunaleApp.vaccination.home.title' },
        loadChildren: () => import('./vaccination/vaccination.module').then(m => m.VaccinationModule),
      },
      {
        path: 'type-vaccin',
        data: { pageTitle: 'projetCommunaleApp.typeVaccin.home.title' },
        loadChildren: () => import('./type-vaccin/type-vaccin.module').then(m => m.TypeVaccinModule),
      },
      {
        path: 'cible',
        data: { pageTitle: 'projetCommunaleApp.cible.home.title' },
        loadChildren: () => import('./cible/cible.module').then(m => m.CibleModule),
      },
      {
        path: 'don-sang',
        data: { pageTitle: 'projetCommunaleApp.donSang.home.title' },
        loadChildren: () => import('./don-sang/don-sang.module').then(m => m.DonSangModule),
      },
      {
        path: 'activite-politique',
        data: { pageTitle: 'projetCommunaleApp.activitePolitique.home.title' },
        loadChildren: () => import('./activite-politique/activite-politique.module').then(m => m.ActivitePolitiqueModule),
      },
      {
        path: 'sensibiisation-internet',
        data: { pageTitle: 'projetCommunaleApp.sensibiisationInternet.home.title' },
        loadChildren: () => import('./sensibiisation-internet/sensibiisation-internet.module').then(m => m.SensibiisationInternetModule),
      },
      {
        path: 'utilisation-internet',
        data: { pageTitle: 'projetCommunaleApp.utilisationInternet.home.title' },
        loadChildren: () => import('./utilisation-internet/utilisation-internet.module').then(m => m.UtilisationInternetModule),
      },
      {
        path: 'logiciel',
        data: { pageTitle: 'projetCommunaleApp.logiciel.home.title' },
        loadChildren: () => import('./logiciel/logiciel.module').then(m => m.LogicielModule),
      },
      {
        path: 'artiste',
        data: { pageTitle: 'projetCommunaleApp.artiste.home.title' },
        loadChildren: () => import('./artiste/artiste.module').then(m => m.ArtisteModule),
      },
      {
        path: 'interviews-artiste',
        data: { pageTitle: 'projetCommunaleApp.interviewsArtiste.home.title' },
        loadChildren: () => import('./interviews-artiste/interviews-artiste.module').then(m => m.InterviewsArtisteModule),
      },
      {
        path: 'evenement',
        data: { pageTitle: 'projetCommunaleApp.evenement.home.title' },
        loadChildren: () => import('./evenement/evenement.module').then(m => m.EvenementModule),
      },
      {
        path: 'vidange',
        data: { pageTitle: 'projetCommunaleApp.vidange.home.title' },
        loadChildren: () => import('./vidange/vidange.module').then(m => m.VidangeModule),
      },
      {
        path: 'collecteur-odeur',
        data: { pageTitle: 'projetCommunaleApp.collecteurOdeur.home.title' },
        loadChildren: () => import('./collecteur-odeur/collecteur-odeur.module').then(m => m.CollecteurOdeurModule),
      },
      {
        path: 'recuperation-recyclable',
        data: { pageTitle: 'projetCommunaleApp.recuperationRecyclable.home.title' },
        loadChildren: () => import('./recuperation-recyclable/recuperation-recyclable.module').then(m => m.RecuperationRecyclableModule),
      },
      {
        path: 'etablissement',
        data: { pageTitle: 'projetCommunaleApp.etablissement.home.title' },
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.EtablissementModule),
      },
      {
        path: 'resultat-examen',
        data: { pageTitle: 'projetCommunaleApp.resultatExamen.home.title' },
        loadChildren: () => import('./resultat-examen/resultat-examen.module').then(m => m.ResultatExamenModule),
      },
      {
        path: 'proposition',
        data: { pageTitle: 'projetCommunaleApp.proposition.home.title' },
        loadChildren: () => import('./proposition/proposition.module').then(m => m.PropositionModule),
      },
      {
        path: 'lien-tutoriel',
        data: { pageTitle: 'projetCommunaleApp.lienTutoriel.home.title' },
        loadChildren: () => import('./lien-tutoriel/lien-tutoriel.module').then(m => m.LienTutorielModule),
      },
      {
        path: 'ensegnant',
        data: { pageTitle: 'projetCommunaleApp.ensegnant.home.title' },
        loadChildren: () => import('./ensegnant/ensegnant.module').then(m => m.EnsegnantModule),
      },
      {
        path: 'entreprenariat',
        data: { pageTitle: 'projetCommunaleApp.entreprenariat.home.title' },
        loadChildren: () => import('./entreprenariat/entreprenariat.module').then(m => m.EntreprenariatModule),
      },
      {
        path: 'entrepreneur',
        data: { pageTitle: 'projetCommunaleApp.entrepreneur.home.title' },
        loadChildren: () => import('./entrepreneur/entrepreneur.module').then(m => m.EntrepreneurModule),
      },
      {
        path: 'demande-interview',
        data: { pageTitle: 'projetCommunaleApp.demandeInterview.home.title' },
        loadChildren: () => import('./demande-interview/demande-interview.module').then(m => m.DemandeInterviewModule),
      },
      {
        path: 'domaine-activite',
        data: { pageTitle: 'projetCommunaleApp.domaineActivite.home.title' },
        loadChildren: () => import('./domaine-activite/domaine-activite.module').then(m => m.DomaineActiviteModule),
      },
      {
        path: 'partenaires',
        data: { pageTitle: 'projetCommunaleApp.partenaires.home.title' },
        loadChildren: () => import('./partenaires/partenaires.module').then(m => m.PartenairesModule),
      },
      {
        path: 'eleveur',
        data: { pageTitle: 'projetCommunaleApp.eleveur.home.title' },
        loadChildren: () => import('./eleveur/eleveur.module').then(m => m.EleveurModule),
      },
      {
        path: 'inscription',
        data: { pageTitle: 'projetCommunaleApp.inscription.home.title' },
        loadChildren: () => import('./inscription/inscription.module').then(m => m.InscriptionModule),
      },
      {
        path: 'calendrier-evenement',
        data: { pageTitle: 'projetCommunaleApp.calendrierEvenement.home.title' },
        loadChildren: () => import('./calendrier-evenement/calendrier-evenement.module').then(m => m.CalendrierEvenementModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
