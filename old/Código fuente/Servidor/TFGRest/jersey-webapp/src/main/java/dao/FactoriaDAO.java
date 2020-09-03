package dao;

public abstract class FactoriaDAO {
	
	private static FactoriaDAO unicaInstancia = null;
	public static final int HIBERNATE = 0;
	
	protected FactoriaDAO() {}
	
	public static FactoriaDAO getInstancia(int tipo) {
		if (unicaInstancia == null){
			switch (tipo) {
				case HIBERNATE:
					unicaInstancia = new FactoriaHibernateDAO();
					break;
				default:
					break;
			}
		}
		return unicaInstancia;
	}
	
	public abstract ProfesorDAO getProfesorDAO();
	public abstract LineaDAO getLineaDAO();
	public abstract EstudianteDAO getEstudianteDAO();
	public abstract ReunionDAO getReunionDAO();
	

}
