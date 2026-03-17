package com.agendadeportistas.agendaservices.controllers;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendadeportistas.agendaservices.entities.ExposureEntity;
import com.agendadeportistas.agendaservices.entities.PostEntity;
import com.agendadeportistas.agendaservices.entities.UsuariosEntity;
import com.agendadeportistas.agendaservices.repositories.ExposureRepository;
import com.agendadeportistas.agendaservices.repositories.PostRepository;
import com.agendadeportistas.agendaservices.repositories.UserRepository;
import com.agendadeportistas.agendaservices.shared.dto.PostDto;
import com.agendadeportistas.agendaservices.shared.dto.PostRespuestaDto;
import com.agendadeportistas.agendaservices.shared.dto.UsuarioRespuestaDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/post/")
public class RestControllerPost {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private ExposureRepository exposureRepository;

    @Autowired
    public RestControllerPost(PostRepository postRepository, UserRepository userRepository,
            ExposureRepository exposureRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.exposureRepository = exposureRepository;
    }

    @PostMapping("crear")
    public ResponseEntity<String> crear(@RequestBody PostDto postDto) {
        PostEntity nuevoPost = new PostEntity();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Se obtienen los datos del usuario conectado con el cual se generó el token
        User usuarioConectado = (User) authentication.getPrincipal();

        UsuariosEntity usuario = userRepository.findByUsername(usuarioConectado.getUsername()).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>("No se encontró el usuario", HttpStatus.BAD_REQUEST);
        }

        ExposureEntity exposure = exposureRepository.findById(postDto.getExposureId()).orElse(null);

        if (exposure == null) {
            return new ResponseEntity<>("No se encontró el tipo de privacidad", HttpStatus.BAD_REQUEST);
        }

        nuevoPost.setTitulo(postDto.getTitulo());
        nuevoPost.setContenido(postDto.getContenido());
        nuevoPost.setUsuario(usuario);
        nuevoPost.setExposure(exposure);

        postRepository.save(nuevoPost);

        return new ResponseEntity<>("Post creado correctamente", HttpStatus.OK);
    }

    @GetMapping("listar")
    public List<PostRespuestaDto> getPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Se obtienen los datos del usuario conectado con el cual se generó el token
        User usuarioConectado = (User) authentication.getPrincipal();

        UsuariosEntity usuario = userRepository.findByUsername(usuarioConectado.getUsername()).orElse(null);

        List<PostEntity> posts = postRepository.getByUsuarioIdUsuarioOrderByCreadoDesc(usuario.getIdUsuario());
        List<PostRespuestaDto> postsResp = new ArrayList<PostRespuestaDto>();

        for (PostEntity post : posts) {
            // Se mapean los datos de la entidad a la DTO
            PostRespuestaDto postResp = new PostRespuestaDto();

            postResp.setTitulo(post.getTitulo());
            postResp.setContenido(post.getContenido());
            postResp.setCreado(post.getCreado());
            // Se obtiene el nombre del usuario al que pertenece el post
            postResp.setUser(new UsuarioRespuestaDto(usuario.getUsername(), usuario.getNombre(), usuario.getEmail()));
            postResp.setExposureId(post.getExposure().getIdExposure());

            postsResp.add(postResp);
        }

        return postsResp;
    }

    @GetMapping("last")
    public List<PostRespuestaDto> getMethodName(@RequestParam String param) {
        // List<PostEntity> posts =
        // postRepository.getByUsuarioIdUsuarioOrderByCreadoDesc(usuario.getIdUsuario());

        return null;
    }

}
