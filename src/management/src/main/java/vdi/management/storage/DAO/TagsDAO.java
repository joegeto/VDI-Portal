package vdi.management.storage.DAO;

import org.hibernate.Session;

import vdi.management.storage.Hibernate;
import vdi.management.storage.HibernateUtil;
import vdi.management.storage.entities.Tag;

/**
 * The DAO for Tags Entity.
 */
public final class TagsDAO {

	/**
	 * Private constructor for static class.
	 */
	private TagsDAO() { }

	/**
	 * Checks if the Tag identified by its name exists.
	 *
	 * @param tag
	 *            the name of the tag
	 * @return true if exists, otherwise false
	 */
	public static boolean exists(String tag) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Long occ = (Long) session
				.createQuery("select count(*) from Tag as t where t.name = ?")
				.setString(0, tag).uniqueResult();

		session.getTransaction().commit();

		return (occ > 0);
	}

	/**
	 * Creates a Tag entity and stores it.
	 *
	 * @param t
	 *            the Tag to store
	 */
	public static void create(Tag t) {
		Hibernate.saveObject(t);
	}

	/**
	 * Updates a database entry for a Tag.
	 *
	 * @param t
	 *            the tag to update
	 */
	public static void update(Tag t) {
		Hibernate.saveOrUpdateObject(t);
	}

	/**
	 * Get a tag by its name.
	 *
	 * @param tag
	 *            the name of the tag as String
	 * @return the Tag corresponding Tag
	 */
	public static Tag get(String tag) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Tag t = (Tag) session.createQuery("from Tag where name=?")
				.setString(0, tag).uniqueResult();

		session.getTransaction().commit();

		return t;
	}

	/**
	 * Select a Tag from Database by its slug.
	 *
	 * @param tagSlug
	 *            the slug
	 * @return the Tag object with specified slug.
	 */
	public static Tag getBySlug(String tagSlug) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Tag t = (Tag) session.createQuery("from Tag where slug=?")
				.setString(0, tagSlug).uniqueResult();

		session.getTransaction().commit();

		return t;
	}

}
