package dao;

public class FactoriaHibernateDAO extends FactoriaDAO {

	@Override
	public ProfesorDAO getProfesorDAO() {
		return new ProfesorHibernateDAO(); 
	}

	@Override
	public LineaDAO getLineaDAO() {
		return new LineaHibernateDAO();
	}

	@Override
	public EstudianteDAO getEstudianteDAO() {
		return new EstudianteHibernateDAO();
	}

	@Override
	public ReunionDAO getReunionDAO() {
		return new ReunionHibernateDAO();
	}

}
